import logging
from quiz_api_client import QuizAPIClient
from user_api_client import UserAPIClient

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler("quiz_api_client.log"),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

GITPOD_BASE_URL = "https://8080-mduc2610-temp-txa6cej4fdy.ws-us119.gitpod.io"
CODESPACE_BASE_URL = "https://upgraded-telegram-9v4jgg9jvjjh465-8080.app.github.dev"

class EnhancedQuizAPIClient:
    """Enhanced version that includes user creation"""
    
    def __init__(self, base_url=f"{GITPOD_BASE_URL}/api"):
        self.quiz_client = QuizAPIClient(base_url)
        self.user_client = UserAPIClient(base_url)
    
    def setup_complete_test_environment(self, user_count=10, clear_existing=True):
        """Setup complete test environment with users and quiz data"""
        logger.info("Setting up complete test environment...")
        
        # Create users first
        logger.info("Creating test users...")
        users = self.user_client.process_user_data(user_count, clear_existing)
        
        if users:
            logger.info(f"Created {len(users)} users successfully")
            
            # Create quiz data
            logger.info("Creating quiz data...")
            quiz_success = self.quiz_client.process_all_data()
            
            if quiz_success:
                logger.info("Complete test environment setup successfully!")
                return True
            else:
                logger.error("Failed to setup quiz data")
                return False
        else:
            logger.error("Failed to create users")
            return False

def main():
    """Main function to demonstrate usage"""
    import argparse
    
    parser = argparse.ArgumentParser(description='User API Client')
    parser.add_argument('--users', action='store_true', help='Create only users')
    parser.add_argument('--quizzes', action='store_true', help='Create only quizzes')
    parser.add_argument('--count', type=int, default=10, help='Number of users to create')
    parser.add_argument('--clear', default=True, action='store_true', help='Clear existing data first')
    # parser.add_argument('--complete', action='store_true', help='Setup complete environment (users + quizzes)')
    
    args = parser.parse_args()
    
    if args.users:
        user_client = UserAPIClient()
        user_client.process_user_data(args.count, args.clear)
    elif args.quizzes:
        quiz_client = QuizAPIClient()
        quiz_client.proccess_all_data()
    else:
        client = EnhancedQuizAPIClient()
        client.setup_complete_test_environment(args.count, args.clear)

if __name__ == "__main__":
    main()