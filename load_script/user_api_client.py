import requests
import json
import random
import os
from pathlib import Path
import time
import logging
from faker import Faker
from datetime import datetime
from dateutil.relativedelta import relativedelta

# Set up logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler("user_api_client.log"),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

class UserAPIClient:
    def __init__(self, base_url):
        self.base_url = base_url
        self.state_file = "created_users_state.json"
        self.state = self._load_state()
        self.fake = Faker()
        self.user_images_folder = "images/users"
        self.available_images = self._get_available_images()
        
    def _load_state(self):
        state_path = Path(self.state_file)
        if state_path.exists():
            logger.info(f"Loading existing state from {self.state_file}")
            with open(state_path, "r") as f:
                try:
                    data = json.load(f)
                    logger.info(f"Loaded state: users created = {len(data.get('users', []))}")
                    return data
                except json.JSONDecodeError:
                    logger.warning(f"Invalid JSON in state file, creating new state")
                    return {"users": []}
        return {"users": []}
    
    def _save_state(self):
        with open(self.state_file, "w") as f:
            json.dump(self.state, f, indent=2)
        logger.info(f"State saved to {self.state_file}")
    
    def _get_available_images(self):
        """Get list of available user avatar images"""
        script_dir = os.path.dirname(os.path.abspath(__file__))
        images_path = os.path.join(script_dir, self.user_images_folder)
        
        if not os.path.exists(images_path):
            logger.warning(f"User images folder not found: {images_path}")
            return []
        
        # Common image extensions
        image_extensions = ['.jpg', '.jpeg', '.png', '.gif', '.webp', '.svg']
        images = []
        
        for file in os.listdir(images_path):
            if any(file.lower().endswith(ext) for ext in image_extensions):
                images.append(os.path.join(self.user_images_folder, file))
        
        logger.info(f"Found {len(images)} avatar images in {self.user_images_folder}")
        return images
    
    def _get_avatar_image(self, user_index):
        """Get avatar image for user with rotation if exceed number of images"""
        if not self.available_images:
            return None
        
        # Rotate through available images
        image_index = user_index % len(self.available_images)
        return self.available_images[image_index]
    
    def _calculate_age(self, date_of_birth):
        """Calculate age from date of birth"""
        try:
            birth_date = datetime.strptime(date_of_birth, "%Y-%m-%d")
            today = datetime.now()
            age = relativedelta(today, birth_date).years
            return age
        except:
            return random.randint(18, 65)
    
    def _generate_fake_user_data(self, user_index):
        """Generate fake user data using Faker"""
        # Generate personal info
        first_name = self.fake.first_name()
        last_name = self.fake.last_name()
        full_name = f"{first_name} {last_name}"
        
        # Generate username (ensure uniqueness by adding index)
        username = f"{first_name.lower()}{last_name.lower()}{user_index}"
        
        # Generate email
        email = f"{username}@{self.fake.domain_name()}"
        
        # Generate date of birth (age between 18-65)
        date_of_birth = self.fake.date_of_birth(minimum_age=18, maximum_age=65)
        date_of_birth_str = date_of_birth.strftime("%Y-%m-%d")
        
        # Calculate age
        age = self._calculate_age(date_of_birth_str)
        
        # Generate country
        country = self.fake.country()
        
        # Generate password
        password = "password"  # Default password for testing
        
        return {
            "username": username,
            "password": password,
            "email": email,
            "fullName": full_name,
            "lastName": last_name,
            "country": country,
            "dateOfBirth": date_of_birth_str,
            "age": age
        }
    
    def create_user(self, user_data, avatar_image_path=None):
        """Create a single user with optional avatar"""
        form_data = {
            "username": user_data["username"],
            "password": user_data["password"],
            "email": user_data["email"],
            "fullName": user_data["fullName"],
            "lastName": user_data["lastName"],
            "country": user_data["country"],
            "dateOfBirth": user_data["dateOfBirth"],
            "age": str(user_data["age"])
        }
        
        files = {}
        file_handle = None
        
        # Handle avatar image
        if avatar_image_path:
            script_dir = os.path.dirname(os.path.abspath(__file__))
            full_image_path = os.path.join(script_dir, avatar_image_path)
            
            if os.path.exists(full_image_path):
                file_handle = open(full_image_path, 'rb')
                files['avatarFile'] = file_handle
                logger.info(f"Using avatar: {avatar_image_path}")
            else:
                logger.warning(f"Avatar image not found: {full_image_path}")
        
        try:
            logger.info(f"Creating user: {user_data['username']} ({user_data['email']})")
            
            response = requests.post(
                f"{self.base_url}/users",
                data=form_data,
                files=files if files else None
            )
            
            if response.status_code in [200, 201]:
                created_user = response.json()
                user_id = created_user.get("id")
                logger.info(f"User created successfully with ID: {user_id}")
                
                # Save to state
                self.state["users"].append({
                    "id": user_id,
                    "username": user_data["username"],
                    "email": user_data["email"],
                    "fullName": user_data["fullName"],
                    "avatar": avatar_image_path
                })
                self._save_state()
                
                return created_user
            else:
                logger.error(f"Failed to create user: {response.status_code}")
                logger.error(f"Response: {response.text}")
                return None
                
        except Exception as e:
            logger.error(f"Error creating user: {e}")
            return None
        finally:
            if file_handle:
                file_handle.close()
    
    def create_multiple_users(self, count=30):
        """Create multiple fake users"""
        logger.info(f"Creating {count} fake users...")
        
        created_users = []
        existing_count = len(self.state["users"])
        
        for i in range(count):
            user_index = existing_count + i
            
            # Generate fake user data
            user_data = self._generate_fake_user_data(user_index)
            
            # Get avatar image (with rotation)
            avatar_image = self._get_avatar_image(user_index)
            
            # Create user
            created_user = self.create_user(user_data, avatar_image)
            
            if created_user:
                created_users.append(created_user)
            
            # Small delay to avoid overwhelming the server
            time.sleep(0.5)
        
        logger.info(f"Successfully created {len(created_users)} users out of {count} attempted")
        return created_users
    
    def get_all_users(self):
        """Get all users from the API"""
        try:
            response = requests.get(f"{self.base_url}/users")
            if response.status_code == 200:
                return response.json()
            else:
                logger.error(f"Failed to fetch users: {response.status_code}")
                return []
        except Exception as e:
            logger.error(f"Error fetching users: {e}")
            return []
    
    def delete_user(self, user_id):
        """Delete a specific user"""
        try:
            response = requests.delete(f"{self.base_url}/users/{user_id}")
            if response.status_code in [200, 204]:
                logger.info(f"Deleted user with ID: {user_id}")
                return True
            else:
                logger.warning(f"Failed to delete user {user_id}: {response.status_code}")
                return False
        except Exception as e:
            logger.error(f"Error deleting user {user_id}: {e}")
            return False
    
    def delete_all_users(self):
        """Delete all users"""
        logger.info("Deleting all existing users...")
        
        users = self.get_all_users()
        deleted_count = 0
        
        for user in users:
            if self.delete_user(user["id"]):
                deleted_count += 1
            time.sleep(0.2)  # Small delay between deletions
        
        # Clear state
        self.state = {"users": []}
        self._save_state()
        
        logger.info(f"Deleted {deleted_count} users successfully")
        return deleted_count
    
    def process_user_data(self, count=10, clear_existing=False):
        """Main method to process user data creation"""
        if clear_existing:
            self.delete_all_users()
        
        return self.create_multiple_users(count)
