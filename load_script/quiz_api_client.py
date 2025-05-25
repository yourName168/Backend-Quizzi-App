import requests
import json
import random
import os
from pathlib import Path
import time
import logging

# Set up logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler("quiz_api_client.log"),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

class QuizAPIClient:
    def __init__(self, base_url):
        self.base_url = base_url
        self.state_file = "created_data_state.json"
        self.data_file = "load_script/data/quiz_data.json"
        self.state = self._load_state()
        
    def _load_state(self):
        state_path = Path(self.state_file)
        if state_path.exists():
            logger.info(f"Loading existing state from {self.state_file}")
            with open(state_path, "r") as f:
                try:
                    data = json.load(f)
                    logger.info(f"Loaded state: {data}")
                    return data
                except json.JSONDecodeError:
                    logger.warning(f"Invalid JSON in state file, creating new state")
                    return {"collections": {}}
        return {"collections": {}}
    
    def _save_state(self):
        with open(self.state_file, "w") as f:
            json.dump(self.state, f, indent=2)
        logger.info(f"State saved to {self.state_file}")
    
    def create_quiz_collection(self, collection_data):
        category = collection_data["category"]
        if category in self.state["collections"]:
            logger.info(f"Skipping already created collection: {category}")
            return self.state["collections"][category]["id"]
        
        form_data = {
            "authorId": collection_data["authorId"],
            "description": collection_data["description"],
            "category": collection_data["category"],
            "visibleTo": collection_data["visibleTo"]
        }
        
        files = {}
        if "coverPhoto" in collection_data and collection_data["coverPhoto"]:
            script_dir = os.path.dirname(os.path.abspath(__file__))
            image_path = os.path.join(script_dir, collection_data["coverPhoto"])
            if os.path.exists(image_path):
                files = {'coverPhotoFile': open(image_path, 'rb')}
            else:
                logger.warning(f"Cover photo file not found: {image_path}")
        
        try:
            logger.info(f"Creating collection: {category}")
            response = requests.post(
                f"{self.base_url}/quiz-collections",
                data=form_data,
                files=files
            )
            
            if response.status_code in [200, 201]:
                created_collection = response.json()
                collection_id = created_collection["id"]
                logger.info(f"Collection created successfully with ID: {collection_id}")
                
                self.state["collections"][category] = {
                    "id": collection_id,
                    "quizzes": {}
                }
                self._save_state()
                
                return collection_id
            else:
                logger.error(f"Failed to create collection: {response.status_code}")
                logger.error(f"Response: {response.text}")
                return None
        except Exception as e:
            logger.error(f"Error creating collection: {e}")
            return None
        finally:
            for file in files.values():
                if hasattr(file, 'close'):
                    file.close()
    
    def create_quiz(self, quiz_data, collection_id):
        title = quiz_data["title"]
        collection_category = None
        
        for category, data in self.state["collections"].items():
            if data["id"] == collection_id:
                collection_category = category
                break
        
        if collection_category and title in self.state["collections"][collection_category]["quizzes"]:
            logger.info(f"Skipping already created quiz: {title}")
            return self.state["collections"][collection_category]["quizzes"][title]["id"]
        
        form_data = {
            "userId": quiz_data["userId"],
            "quizCollectionId": str(collection_id),  # Ensure it's a string as expected by the API
            "title": quiz_data["title"],
            "description": quiz_data["description"],
            "keyword": quiz_data["keyword"],
            "visible": quiz_data["visible"],
            "visibleQuizQuestion": quiz_data["visibleQuizQuestion"],
            "shuffle": quiz_data["shuffle"]
        }
        
        files = {}
        if "coverPhoto" in quiz_data and quiz_data["coverPhoto"]:
            script_dir = os.path.dirname(os.path.abspath(__file__))
            image_path = os.path.join(script_dir, quiz_data["coverPhoto"])
            if os.path.exists(image_path):
                files = {'coverPhotoFile': open(image_path, 'rb')}
            else:
                logger.warning(f"Cover photo file not found: {image_path}")
        
        try:
            logger.info(f"Creating quiz: {title} for collection ID: {collection_id}")
            response = requests.post(
                f"{self.base_url}/quizzes",
                data=form_data,
                files=files
            )
            
            if response.status_code in [200, 201]:
                created_quiz = response.json()
                quiz_id = created_quiz["id"]
                logger.info(f"Quiz created successfully with ID: {quiz_id}")
                
                if collection_category:
                    self.state["collections"][collection_category]["quizzes"][title] = {
                        "id": quiz_id,
                        "questions": {}
                    }
                    self._save_state()
                
                return quiz_id
            else:
                logger.error(f"Failed to create quiz: {response.status_code}")
                logger.error(f"Response: {response.text}")
                return None
        except Exception as e:
            logger.error(f"Error creating quiz: {e}")
            return None
        finally:
            for file in files.values():
                if hasattr(file, 'close'):
                    file.close()
    
   
    def create_individual_question(self, quiz_id, question_data):
        question_types = requests.get(f"{self.base_url}/question-types")
        if question_types.status_code == 200:
            question_types = question_types.json()
        else:
            logger.error(f"Failed to fetch question types: {question_types.status_code}")
            return None

        question_type_id = question_data.get("questionTypeId")
        if not question_type_id:
            logger.error("Question type ID is required")
            return None

        endpoint_map = {
            1: "single-choice",
            2: "multi-choice",
            3: "text",
            4: "puzzle",
            5: "true-false",
            6: "slider"
        }

        def get_real_question_type_id(question_type_id):
            question_type = endpoint_map.get(question_type_id)
            if question_type == "single-choice":
                return [x for x in question_types if x["name"] == "SINGLE_CHOICE"][0]["id"]
            elif question_type == "multi-choice":
                return [x for x in question_types if x["name"] == "MULTI_CHOICE"][0]["id"]
            elif question_type == "text":
                return [x for x in question_types if x["name"] == "TEXT"][0]["id"]
            elif question_type == "puzzle":
                return [x for x in question_types if x["name"] == "PUZZLE"][0]["id"]
            elif question_type == "true-false":
                return [x for x in question_types if x["name"] == "TRUE_FALSE"][0]["id"]
            elif question_type == "slider":
                return [x for x in question_types if x["name"] == "SLIDER"][0]["id"]
            return None

        endpoint = endpoint_map.get(question_type_id)
        if not endpoint:
            logger.error(f"Unknown question type ID: {question_type_id}")
            return None

        pointList = [50, 100, 200, 250, 500, 750, 1000, 2000]
        timeLimitList = [5, 10, 20, 30, 45, 60, 90, 120]
       
        form_data = {
            "quizId": str(quiz_id),
            "questionTypeId": str(get_real_question_type_id(question_type_id)),
            "position": str(question_data.get("position", 1)),
            "content": question_data.get("content", ""),
            "point": str(random.choice(pointList)),
            "timeLimit": str(random.choice(timeLimitList)),
            "description": question_data.get("description", "")
        }

        if endpoint == "true-false":
            form_data["correctAnswer"] = bool(question_data.get("correctAnswer", False))
        elif endpoint == "slider":
            form_data["minValue"] = str(question_data.get("minValue", 0))
            form_data["maxValue"] = str(question_data.get("maxValue", 100))
            form_data["defaultValue"] = str(question_data.get("defaultValue", 50))
            form_data["correctAnswer"] = str(question_data.get("correctAnswer", 50))
            form_data["color"] = question_data.get("color", "#2196F3")

        files = {}
        file_handles = []
        if question_data.get("image") and not question_data["image"].startswith("http"):
            script_dir = os.path.dirname(os.path.abspath(__file__))
            image_path = os.path.join(script_dir, question_data["image"])
            if os.path.exists(image_path):
                file_handle = open(image_path, 'rb')
                file_handles.append(file_handle)
                files['imageFile'] = file_handle
            else:
                logger.warning(f"Question image file not found: {image_path}")

        if question_data.get("audio") and not question_data["audio"].startswith("http"):
            script_dir = os.path.dirname(os.path.abspath(__file__))
            audio_path = os.path.join(script_dir, question_data["audio"])
            if os.path.exists(audio_path):
                file_handle = open(audio_path, 'rb')
                file_handles.append(file_handle)
                files['audioFile'] = file_handle
            else:
                logger.warning(f"Question audio file not found: {audio_path}")

        if endpoint in ["single-choice", "multi-choice"]:
            choice_options = question_data.get("choiceOptions", [])
            for i, choice in enumerate(choice_options):
                if 'text' in choice:
                    form_data[f"choiceOptions[{i}].text"] = choice['text']
                if 'isCorrect' in choice:
                    form_data[f"choiceOptions[{i}].isCorrect"] = str(choice['isCorrect']).lower()
                if 'image' in choice and choice['image'] and not choice['image'].startswith("http"):
                    script_dir = os.path.dirname(os.path.abspath(__file__))
                    img_path = os.path.join(script_dir, choice['image'])
                    if os.path.exists(img_path):
                        files[f'choiceOptions[{i}].imageFile'] = open(img_path, 'rb')
                        file_handles.append(files[f'choiceOptions[{i}].imageFile'])
                if 'audio' in choice and choice['audio'] and not choice['audio'].startswith("http"):
                    script_dir = os.path.dirname(os.path.abspath(__file__))
                    audio_path = os.path.join(script_dir, choice['audio'])
                    if os.path.exists(audio_path):
                        files[f'choiceOptions[{i}].audioFile'] = open(audio_path, 'rb')
                        file_handles.append(files[f'choiceOptions[{i}].audioFile'])
            endpoint = "choice"  
            
        elif endpoint == "puzzle":
            puzzle_pieces = question_data.get("puzzlePieces", [])
            for i, piece in enumerate(puzzle_pieces):
                if 'text' in piece:
                    form_data[f"puzzlePieces[{i}].text"] = piece['text']
                if 'correctPosition' in piece:
                    form_data[f"puzzlePieces[{i}].correctPosition"] = str(piece['correctPosition'])
                if 'image' in piece and piece['image'] and not piece['image'].startswith("http"):
                    script_dir = os.path.dirname(os.path.abspath(__file__))
                    img_path = os.path.join(script_dir, piece['image'])
                    if os.path.exists(img_path):
                        files[f'puzzlePieces[{i}].imageFile'] = open(img_path, 'rb')
                        file_handles.append(files[f'puzzlePieces[{i}].imageFile'])
                if 'audio' in piece and piece['audio'] and not piece['audio'].startswith("http"):
                    script_dir = os.path.dirname(os.path.abspath(__file__))
                    audio_path = os.path.join(script_dir, piece['audio'])
                    if os.path.exists(audio_path):
                        files[f'puzzlePieces[{i}].audioFile'] = open(audio_path, 'rb')
                        file_handles.append(files[f'puzzlePieces[{i}].audioFile'])
        
        elif endpoint == "text":
            accepted_answers = question_data.get("acceptedAnswers", [])
            for i, ans in enumerate(accepted_answers):
                if 'text' in ans:
                    form_data[f"acceptedAnswers[{i}].text"] = ans['text']
            form_data["caseSensitive"] = bool(question_data.get("caseSensitive", False))

        try:
            if files:
                response = requests.post(
                    f"{self.base_url}/questions/{endpoint}",
                    data=form_data,
                    files=files,
                )
            else:
                dummy_files = {'dummy': ('', '')}
                response = requests.post(
                    f"{self.base_url}/questions/{endpoint}",
                    data=form_data,
                    files=dummy_files,
                )

            if response.status_code in [200, 201]:
                created_question = response.json()
                logger.info(f"Question created successfully with ID: {created_question['id']}")
                return created_question
            else:
                logger.error(f"Failed to create question: {response.status_code}")
                logger.error(f"Response: {response.text}")
                return None
        except Exception as e:
            logger.error(f"Error creating question: {e}")
            return None
        finally:
            for file_handle in file_handles:
                if hasattr(file_handle, 'close'):
                    file_handle.close()

    
    def process_all_data(self):
        self.delete_all_data()
        data_path = Path(self.data_file)
        if not data_path.exists():
            logger.error(f"Data file {self.data_file} not found")
            return False
        
        with open(data_path, "r", encoding="utf-8") as f:
            data = json.load(f)
        
        if "collections" in data:
            collections = data["collections"]
        else:
            collections = data  
        
        for collection_data in collections:
            collection_id = self.create_quiz_collection(collection_data)
            
            if collection_id:
                quizzes = collection_data.get("quizzes", [])
                for quiz_data in quizzes:
                    time.sleep(0.5)  
                    quiz_id = self.create_quiz(quiz_data, collection_id)
                    
                    if quiz_id and "questions" in quiz_data and quiz_data["questions"]:
                        time.sleep(0.5)  
                        for question_data in quiz_data["questions"]:
                            time.sleep(0.3)  
                            self.create_individual_question(quiz_id, question_data)
            
            time.sleep(1)  
        
        logger.info("Data processing completed")
        return True

    
    def get_all_collections(self):
        try:
            response = requests.get(f"{self.base_url}/quiz-collections")
            if response.status_code == 200:
                return response.json()
            else:
                logger.error(f"Failed to fetch collections: {response.status_code}")
                return []
        except Exception as e:
            logger.error(f"Error fetching collections: {e}")
            return []

    def get_all_quizzes(self):
        try:
            response = requests.get(f"{self.base_url}/quizzes")
            if response.status_code == 200:
                return response.json()
            else:
                logger.error(f"Failed to fetch quizzes: {response.status_code}")
                return []
        except Exception as e:
            logger.error(f"Error fetching quizzes: {e}")
            return []

    def get_all_questions(self):
        try:
            response = requests.get(f"{self.base_url}/questions")
            if response.status_code == 200:
                return response.json()
            else:
                logger.error(f"Failed to fetch questions: {response.status_code}")
                return []
        except Exception as e:
            logger.error(f"Error fetching questions: {e}")
            return []
           
    def delete_quiz(self, quiz_id):
        try:
            self.delete_quiz_questions(quiz_id)
            
            response = requests.delete(f"{self.base_url}/quizzes/{quiz_id}")
            if response.status_code in [200, 204]:
                logger.info(f"Deleted quiz with ID: {quiz_id}")
            else:
                logger.warning(f"Failed to delete quiz {quiz_id}: {response.status_code}")
        except Exception as e:
            logger.error(f"Error deleting quiz {quiz_id}: {e}")
            
    def delete_quiz_questions(self, quiz_id):
        try:
            response = requests.delete(f"{self.base_url}/questions/quiz/{quiz_id}")
            if response.status_code in [200, 204]:
                logger.info(f"Deleted all questions for quiz ID: {quiz_id}")
            else:
                logger.warning(f"Failed to delete questions for quiz {quiz_id}: {response.status_code}")
        except Exception as e:
            logger.error(f"Error deleting questions for quiz {quiz_id}: {e}")

    def delete_collection(self, collection_id):
        try:
            quizzes = self.get_all_quizzes()
            for quiz in quizzes:
                if str(quiz.get("quizCollectionId")) == str(collection_id):
                    self.delete_quiz(quiz["id"])
                    time.sleep(0.2)
            
            response = requests.delete(f"{self.base_url}/quiz-collections/{collection_id}")
            if response.status_code in [200, 204]:
                logger.info(f"Deleted collection with ID: {collection_id}")
            else:
                logger.warning(f"Failed to delete collection {collection_id}: {response.status_code}")
        except Exception as e:
            logger.error(f"Error deleting collection {collection_id}: {e}")

    def delete_all_data(self):
        logger.info("Deleting all existing data...")
        
        quizzes = self.get_all_quizzes()
        
        for quiz in quizzes:
            self.delete_quiz(quiz["id"])
            time.sleep(0.2)

        collections = self.get_all_collections()
        for collection in collections:
            self.delete_collection(collection["id"])
            time.sleep(0.5)

    

        self.state = {"collections": {}}
        self._save_state()
        
        logger.info("All data deleted successfully")
