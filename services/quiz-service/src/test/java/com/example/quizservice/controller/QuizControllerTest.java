package com.example.quizservice.controller;

import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.service.QuizService;
import com.example.quizservice.service.UrlService;
import com.example.quizservice.testutils.TestDataFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @Mock
    private UrlService urlService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private QuizController controller;

    private Quiz mockQuiz;

    @BeforeEach
    void setUp() {
        mockQuiz = TestDataFactory.createMockQuiz(1L);
    }

    // @Test
    // @DisplayName("Kiểm tra lấy Quiz theo ID thành công")
    // void getQuizById_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: getQuizById_Success =====");
        
    //     Long id = 1L;
    //     when(quizService.getQuizById(id)).thenReturn(Optional.of(mockQuiz));
        
    //     when(urlService.getCompleteFileUrl(eq("quizzes/1/cover.jpg"), any(HttpServletRequest.class)))
    //         .thenReturn("http://localhost:8082/files/quizzes/1/cover.jpg");
        
    //     System.out.println("Đang gọi controller.getQuizById");
    //     ResponseEntity<Quiz> response = controller.getQuizById(id, request);
        
    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code phải là OK");
        
    //     verify(quizService).getQuizById(id);
    //     verify(urlService).getCompleteFileUrl("quizzes/1/cover.jpg", request);
        
    //     System.out.println("===== KẾT THÚC TEST: getQuizById_Success =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra lấy Quiz theo ID không tồn tại")
    // void getQuizById_NotFound() {
    //     System.out.println("===== BẮT ĐẦU TEST: getQuizById_NotFound =====");
        
    //     Long id = 999L;
    //     when(quizService.getQuizById(id)).thenReturn(Optional.empty());
        
    //     System.out.println("Đang gọi controller.getQuizById với ID không tồn tại");
    //     ResponseEntity<Quiz> response = controller.getQuizById(id, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là NOT_FOUND");
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code phải là NOT_FOUND");
        
    //     verify(quizService).getQuizById(id);
    //     verify(urlService, never()).getCompleteFileUrl(any(), any());
        
    //     System.out.println("===== KẾT THÚC TEST: getQuizById_NotFound =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra lấy tất cả Quiz thành công")
    // void getAllQuizzes_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: getAllQuizzes_Success =====");
        
    //     List<Quiz> quizzes = Arrays.asList(
    //         TestDataFactory.createMockQuiz(1L),
    //         TestDataFactory.createMockQuiz(2L)
    //     );
    //     when(quizService.getAllQuizzes()).thenReturn(quizzes);
        
    //     for (Quiz quiz : quizzes) {
    //         when(urlService.getCompleteFileUrl(eq(quiz.getCoverPhoto()), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + quiz.getCoverPhoto());
    //     }
        
    //     System.out.println("Đang gọi controller.getAllQuizzes");
    //     ResponseEntity<List<Quiz>> response = controller.getAllQuizzes(request);

    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     verify(quizService).getAllQuizzes();
    //     verify(urlService, times(2)).getCompleteFileUrl(any(), any());
        
    //     System.out.println("===== KẾT THÚC TEST: getAllQuizzes_Success =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra lấy Quiz mới nhất thành công")
    // void getLatestQuizzes_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: getLatestQuizzes_Success =====");
        
    //     List<Quiz> latestQuizzes = Arrays.asList(
    //         TestDataFactory.createMockQuiz(1L),
    //         TestDataFactory.createMockQuiz(2L)
    //     );
    //     when(quizService.getLatestQuizzes()).thenReturn(latestQuizzes);
        
    //     for (Quiz quiz : latestQuizzes) {
    //         when(urlService.getCompleteFileUrl(eq(quiz.getCoverPhoto()), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + quiz.getCoverPhoto());
    //     }
        
    //     System.out.println("Đang gọi controller.getLatestQuizzes");
    //     ResponseEntity<List<Quiz>> response = controller.getLatestQuizzes(request);

    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(2, response.getBody().size());
    //     verify(quizService).getLatestQuizzes();
    //     verify(urlService, times(2)).getCompleteFileUrl(any(), any());
        
    //     System.out.println("===== KẾT THÚC TEST: getLatestQuizzes_Success =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra lấy Quiz theo userId thành công")
    // void getQuizzesByUserId_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: getQuizzesByUserId_Success =====");
        
    //     Long userId = 1L;
    //     List<Quiz> userQuizzes = Arrays.asList(
    //         TestDataFactory.createMockQuiz(1L),
    //         TestDataFactory.createMockQuiz(2L)
    //     );
    //     when(quizService.getQuizzesByUserId(userId)).thenReturn(userQuizzes);
        
    //     for (Quiz quiz : userQuizzes) {
    //         when(urlService.getCompleteFileUrl(eq(quiz.getCoverPhoto()), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + quiz.getCoverPhoto());
    //     }
        
    //     System.out.println("Đang gọi controller.getQuizzesByUserId");
    //     ResponseEntity<List<Quiz>> response = controller.getQuizzesByUserId(userId, request);

    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(2, response.getBody().size());
    //     verify(quizService).getQuizzesByUserId(userId);
    //     verify(urlService, times(2)).getCompleteFileUrl(any(), any());
        
    //     System.out.println("===== KẾT THÚC TEST: getQuizzesByUserId_Success =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra lấy Quiz công khai thành công")
    // void getPublicQuizzes_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: getPublicQuizzes_Success =====");
        
    //     List<Quiz> publicQuizzes = Arrays.asList(
    //         TestDataFactory.createMockQuiz(1L),
    //         TestDataFactory.createMockQuiz(2L)
    //     );
    //     when(quizService.getPublicQuizzes()).thenReturn(publicQuizzes);
        
    //     for (Quiz quiz : publicQuizzes) {
    //         when(urlService.getCompleteFileUrl(eq(quiz.getCoverPhoto()), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + quiz.getCoverPhoto());
    //     }
        
    //     System.out.println("Đang gọi controller.getPublicQuizzes");
    //     ResponseEntity<List<Quiz>> response = controller.getPublicQuizzes(request);

    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(2, response.getBody().size());
    //     verify(quizService).getPublicQuizzes();
    //     verify(urlService, times(2)).getCompleteFileUrl(any(), any());
        
    //     System.out.println("===== KẾT THÚC TEST: getPublicQuizzes_Success =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra tìm kiếm Quiz theo tiêu đề thành công")
    // void searchQuizzes_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: searchQuizzes_Success =====");
        
    //     String searchTerm = "quiz";
    //     List<Quiz> searchResults = Arrays.asList(
    //         TestDataFactory.createMockQuiz(1L),
    //         TestDataFactory.createMockQuiz(2L)
    //     );
    //     when(quizService.searchQuizzes(searchTerm)).thenReturn(searchResults);
        
    //     for (Quiz quiz : searchResults) {
    //         when(urlService.getCompleteFileUrl(eq(quiz.getCoverPhoto()), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + quiz.getCoverPhoto());
    //     }
        
    //     System.out.println("Đang gọi controller.searchQuizzes");
    //     ResponseEntity<List<Quiz>> response = controller.searchQuizzes(searchTerm, request);

    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(2, response.getBody().size());
    //     verify(quizService).searchQuizzes(searchTerm);
    //     verify(urlService, times(2)).getCompleteFileUrl(any(), any());
        
    //     System.out.println("===== KẾT THÚC TEST: searchQuizzes_Success =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra tạo Quiz với file ảnh thành công")
    // void createQuiz_WithImage_Success() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: createQuiz_WithImage_Success =====");
        
    //     System.out.println("Chuẩn bị dữ liệu test cho createQuiz");
    //     QuizDTO dto = TestDataFactory.createQuizDTOWithImage();
        
    //     String expectedPhotoPath = "quizzes/1/cover.jpg";
    //     mockQuiz.setCoverPhoto(expectedPhotoPath);
        
    //     when(quizService.createQuiz(any(QuizDTO.class), any(MultipartFile.class)))
    //             .thenReturn(mockQuiz);
                
    //     when(urlService.getCompleteFileUrl(eq(expectedPhotoPath), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + expectedPhotoPath);

    //     System.out.println("Đang gọi controller.createQuiz");
    //     ResponseEntity<Quiz> response = controller.createQuiz(dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về từ createQuiz");
    //     assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code phải là CREATED");
    //     assertNotNull(response.getBody(), "Body không được null");
    //     assertEquals(1L, response.getBody().getId(), "ID phải là 1");
        
    //     verify(urlService).getCompleteFileUrl(expectedPhotoPath, request);
    //     System.out.println("URL cho file ảnh đã được xây dựng đúng định dạng");
        
    //     System.out.println("===== KẾT THÚC TEST: createQuiz_WithImage_Success =====");
    // }
    
    // @Test
    // @DisplayName("Kiểm tra trường hợp tạo Quiz thất bại do lỗi IO")
    // void createQuiz_IOError() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: createQuiz_IOError =====");
        
    //     QuizDTO dto = TestDataFactory.createQuizDTOWithImage();
    //     when(quizService.createQuiz(any(QuizDTO.class), any(MultipartFile.class)))
    //             .thenThrow(new IOException("Lỗi khi lưu file"));
                
    //     System.out.println("Đang gọi controller.createQuiz với dữ liệu gây lỗi IO");
    //     ResponseEntity<Quiz> response = controller.createQuiz(dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là INTERNAL_SERVER_ERROR");
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: createQuiz_IOError =====");
    // }

    @Test
    @DisplayName("Kiểm tra cập nhật Quiz với file ảnh thành công")
    void updateQuiz_WithImage_Success() throws IOException {
        System.out.println("===== BẮT ĐẦU TEST: updateQuiz_WithImage_Success =====");
        
        System.out.println("Chuẩn bị dữ liệu test cho updateQuiz");
        Long id = 1L;
        QuizDTO dto = TestDataFactory.createQuizDTOForUpdateWithImage(id);
        
        String updatedCoverPath = "quizzes/1/updated-cover.jpg";
        mockQuiz.setTitle("Bài quiz đã cập nhật");
        mockQuiz.setDescription("Mô tả bài quiz đã cập nhật");
        mockQuiz.setCoverPhoto(updatedCoverPath);
        
        when(quizService.updateQuiz(eq(id), any(QuizDTO.class), any(MultipartFile.class)))
                .thenReturn(mockQuiz);
        
        when(urlService.getCompleteFileUrl(eq(updatedCoverPath), any(HttpServletRequest.class)))
                .thenReturn("http://localhost:8082/files/" + updatedCoverPath);
                
        System.out.println("Đang gọi controller.updateQuiz");
        ResponseEntity<Quiz> response = controller.updateQuiz(id, dto, request);
        
        System.out.println("Kiểm tra kết quả trả về từ updateQuiz");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code phải là OK");
        assertNotNull(response.getBody(), "Body không được null");
        assertEquals("Bài quiz đã cập nhật", response.getBody().getTitle(), 
                "Title phải khớp với dữ liệu đã cập nhật");
                
        verify(urlService).getCompleteFileUrl(updatedCoverPath, request);
        
        System.out.println("URL cho file ảnh đã được xây dựng đúng định dạng");
        
        System.out.println("===== KẾT THÚC TEST: updateQuiz_WithImage_Success =====");
    }
    
    // @Test
    // @DisplayName("Kiểm tra cập nhật Quiz không tồn tại")
    // void updateQuiz_NotFound() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: updateQuiz_NotFound =====");
        
    //     Long id = 999L;  
    //     QuizDTO dto = TestDataFactory.createQuizDTOForUpdateWithImage(id);
        
    //     when(quizService.updateQuiz(eq(id), any(QuizDTO.class), any(MultipartFile.class)))
    //             .thenThrow(new RuntimeException("Quiz not found with id: " + id));
                
    //     System.out.println("Đang gọi controller.updateQuiz với ID không tồn tại");
    //     ResponseEntity<Quiz> response = controller.updateQuiz(id, dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là NOT_FOUND");
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: updateQuiz_NotFound =====");
    // }
    
    // @Test
    // @DisplayName("Kiểm tra cập nhật Quiz thất bại do lỗi IO")
    // void updateQuiz_IOError() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: updateQuiz_IOError =====");
        
    //     Long id = 1L;
    //     QuizDTO dto = TestDataFactory.createQuizDTOForUpdateWithImage(id);
        
    //     when(quizService.updateQuiz(eq(id), any(QuizDTO.class), any(MultipartFile.class)))
    //             .thenThrow(new IOException("Lỗi khi cập nhật file"));
                
    //     System.out.println("Đang gọi controller.updateQuiz với dữ liệu gây lỗi IO");
    //     ResponseEntity<Quiz> response = controller.updateQuiz(id, dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là INTERNAL_SERVER_ERROR");
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: updateQuiz_IOError =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra xóa Quiz thành công")
    // void deleteQuiz_Success() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: deleteQuiz_Success =====");
        
    //     Long id = 1L;
    //     doNothing().when(quizService).deleteQuiz(id);
        
    //     System.out.println("Đang gọi controller.deleteQuiz");
    //     ResponseEntity<Void> response = controller.deleteQuiz(id);
        
    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status code phải là NO_CONTENT");
        
    //     verify(quizService).deleteQuiz(id);
        
    //     System.out.println("===== KẾT THÚC TEST: deleteQuiz_Success =====");
    // }
    
    // @Test
    // @DisplayName("Kiểm tra xóa Quiz không tồn tại")
    // void deleteQuiz_NotFound() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: deleteQuiz_NotFound =====");
        
    //     Long id = 999L;
    //     doThrow(new RuntimeException("Quiz not found with id: " + id))
    //         .when(quizService).deleteQuiz(id);
        
    //     System.out.println("Đang gọi controller.deleteQuiz với ID không tồn tại");
    //     ResponseEntity<Void> response = controller.deleteQuiz(id);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là NOT_FOUND");
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: deleteQuiz_NotFound =====");
    // }
    
    // @Test
    // @DisplayName("Kiểm tra xóa Quiz thất bại do lỗi IO")
    // void deleteQuiz_IOError() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: deleteQuiz_IOError =====");
        
    //     Long id = 1L;
    //     doThrow(new IOException("Lỗi khi xóa file"))
    //         .when(quizService).deleteQuiz(id);
        
    //     System.out.println("Đang gọi controller.deleteQuiz với lỗi IO");
    //     ResponseEntity<Void> response = controller.deleteQuiz(id);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là INTERNAL_SERVER_ERROR");
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: deleteQuiz_IOError =====");
    // }
}