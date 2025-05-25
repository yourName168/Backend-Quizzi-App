package com.example.quizservice.controller;

import com.example.quizservice.dto.QuizCollectionDTO;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.service.QuizCollectionService;
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
import org.springframework.mock.web.MockMultipartFile;
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
class QuizCollectionControllerTest {

    @Mock
    private QuizCollectionService quizCollectionService;

    @Mock
    private QuizService quizService;

    @Mock
    private UrlService urlService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private QuizCollectionController controller;

    private QuizCollection mockCollection;

    @BeforeEach
    void setUp() {
        mockCollection = TestDataFactory.createMockQuizCollection(1L);
    }

    // @Test
    // @DisplayName("Kiểm tra lấy QuizCollection theo ID thành công")
    // void getQuizCollectionById_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: getQuizCollectionById_Success =====");
        
    //     Long id = 1L;
    //     when(quizCollectionService.getQuizCollectionById(id)).thenReturn(Optional.of(mockCollection));
        
    //     when(urlService.getCompleteFileUrl(eq("quiz-collections/1/cover.jpg"), any(HttpServletRequest.class)))
    //         .thenReturn("http://localhost:8082/files/quiz-collections/1/cover.jpg");
        
    //     when(quizService.getQuizzesByCollection(any(QuizCollection.class))).thenReturn(Collections.emptySet());
        
    //     System.out.println("Đang gọi controller.getQuizCollectionById");
    //     ResponseEntity<QuizCollection> response = controller.getQuizCollectionById(id, request);
        
    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code phải là OK");
        
    //     verify(quizCollectionService).getQuizCollectionById(id);
    //     verify(urlService).getCompleteFileUrl("quiz-collections/1/cover.jpg", request);
    //     verify(quizService).getQuizzesByCollection(mockCollection); 
        
    //     System.out.println("===== KẾT THÚC TEST: getQuizCollectionById_Success =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra lấy QuizCollection theo ID không tồn tại")
    // void getQuizCollectionById_NotFound() {
    //     System.out.println("===== BẮT ĐẦU TEST: getQuizCollectionById_NotFound =====");
        
    //     Long id = 999L;
    //     when(quizCollectionService.getQuizCollectionById(id)).thenReturn(Optional.empty());
        
    //     System.out.println("Đang gọi controller.getQuizCollectionById với ID không tồn tại");
    //     ResponseEntity<QuizCollection> response = controller.getQuizCollectionById(id, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là NOT_FOUND");
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code phải là NOT_FOUND");
    //     assertNull(response.getBody(), "Body phải là null");
        
    //     verify(quizCollectionService).getQuizCollectionById(id);
    //     verify(quizService, never()).getQuizzesByCollection(any()); // Verify quiz service was never called
    //     verify(urlService, never()).getCompleteFileUrl(any(), any());
        
    //     System.out.println("===== KẾT THÚC TEST: getQuizCollectionById_NotFound =====");
    // }

    // @Test
    // void getAllQuizCollections_Success() {
    //     System.out.println("===== BẮT ĐẦU TEST: getAllQuizCollections_Success =====");
        
    //     List<QuizCollection> collections = Arrays.asList(
    //         createMockCollection(1L),
    //         createMockCollection(2L)
    //     );
    //     when(quizCollectionService.getAllQuizCollections()).thenReturn(collections);
        
    //     for (QuizCollection collection : collections) {
    //         when(urlService.getCompleteFileUrl(eq(collection.getCoverPhoto()), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + collection.getCoverPhoto());
    //     }
        
    //     when(quizService.getQuizzesByCollection(any(QuizCollection.class))).thenReturn(Collections.emptySet());

    //     System.out.println("Đang gọi controller.getAllQuizCollections");
    //     ResponseEntity<List<QuizCollection>> response = controller.getAllQuizCollections(request);

    //     System.out.println("Kiểm tra kết quả trả về");
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     verify(quizCollectionService).getAllQuizCollections();
    //     verify(quizService, times(2)).getQuizzesByCollection(any(QuizCollection.class));
        
    //     System.out.println("===== KẾT THÚC TEST: getAllQuizCollections_Success =====");
    // }

    @Test
    @DisplayName("Kiểm tra tạo QuizCollection với file ảnh")
    void createQuizCollection_WithImage_Success() throws IOException {
        System.out.println("===== BẮT ĐẦU TEST: createQuizCollection_WithImage_Success =====");
        
        System.out.println("Chuẩn bị dữ liệu test cho createQuizCollection");
        QuizCollectionDTO dto = TestDataFactory.createQuizCollectionDTOWithImage();
        
        String expectedPhotoPath = "quiz-collections/1/cover.jpg";
        mockCollection.setCoverPhoto(expectedPhotoPath);
        
        when(quizCollectionService.createQuizCollection(any(QuizCollectionDTO.class), any(MultipartFile.class)))
                .thenReturn(mockCollection);
                
        when(urlService.getCompleteFileUrl(eq(expectedPhotoPath), any(HttpServletRequest.class)))
                .thenReturn("http://localhost:8082/files/" + expectedPhotoPath);

        System.out.println("Đang gọi controller.createQuizCollection");
        ResponseEntity<QuizCollection> response = controller.createQuizCollection(dto, request);
        
        System.out.println("Kiểm tra kết quả trả về từ createQuizCollection");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code phải là CREATED");
        assertNotNull(response.getBody(), "Body không được null");
        assertEquals(1L, response.getBody().getId(), "ID phải là 1");
        
        verify(urlService).getCompleteFileUrl(expectedPhotoPath, request);
        System.out.println("URL cho file ảnh đã được xây dựng đúng định dạng");
        
        System.out.println("===== KẾT THÚC TEST: createQuizCollection_WithImage_Success =====");
    }
    
    // @Test
    // @DisplayName("Kiểm tra trường hợp tạo QuizCollection thất bại do lỗi IO")
    // void createQuizCollection_IOError() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: createQuizCollection_IOError =====");
        
    //     QuizCollectionDTO dto = TestDataFactory.createQuizCollectionDTOWithImage();
    //     when(quizCollectionService.createQuizCollection(any(QuizCollectionDTO.class), any(MultipartFile.class)))
    //             .thenThrow(new IOException("Lỗi khi lưu file"));
                
    //     System.out.println("Đang gọi controller.createQuizCollection với dữ liệu gây lỗi IO");
    //     ResponseEntity<QuizCollection> response = controller.createQuizCollection(dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là INTERNAL_SERVER_ERROR");
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: createQuizCollection_IOError =====");
    // }

    // @Test
    // @DisplayName("Kiểm tra cập nhật QuizCollection với file ảnh")
    // void updateQuizCollection_WithImage_Success() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: updateQuizCollection_WithImage_Success =====");
        
    //     System.out.println("Chuẩn bị dữ liệu test cho updateQuizCollection");
    //     Long id = 1L;
    //     QuizCollectionDTO dto = TestDataFactory.createQuizCollectionDTOForUpdateWithImage(id);
        
    //     String updatedCoverPath = "quiz-collections/1/updated-cover.jpg";
    //     mockCollection.setDescription("Bộ sưu tập bài quiz đã cập nhật");
    //     mockCollection.setCoverPhoto(updatedCoverPath);
        
    //     when(quizCollectionService.updateQuizCollection(eq(id), any(QuizCollectionDTO.class), any(MultipartFile.class)))
    //             .thenReturn(mockCollection);
        
    //     when(urlService.getCompleteFileUrl(eq(updatedCoverPath), any(HttpServletRequest.class)))
    //             .thenReturn("http://localhost:8082/files/" + updatedCoverPath);
                
    //     System.out.println("Đang gọi controller.updateQuizCollection");
    //     ResponseEntity<QuizCollection> response = controller.updateQuizCollection(id, dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về từ updateQuizCollection");
    //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code phải là OK");
    //     assertNotNull(response.getBody(), "Body không được null");
    //     assertEquals("Bộ sưu tập bài quiz đã cập nhật", response.getBody().getDescription(), 
    //             "Description phải khớp với dữ liệu đã cập nhật");
                
    //     verify(urlService).getCompleteFileUrl(updatedCoverPath, request);
        
    //     System.out.println("URL cho file ảnh đã được xây dựng đúng định dạng");
        
    //     System.out.println("===== KẾT THÚC TEST: updateQuizCollection_WithImage_Success =====");
    // }
    
    // @Test
    // @DisplayName("Kiểm tra cập nhật QuizCollection không tồn tại")
    // void updateQuizCollection_NotFound() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: updateQuizCollection_NotFound =====");
        
    //     Long id = 999L;  
    //     QuizCollectionDTO dto = TestDataFactory.createQuizCollectionDTOForUpdateWithImage(id);
        
    //     when(quizCollectionService.updateQuizCollection(eq(id), any(QuizCollectionDTO.class), any(MultipartFile.class)))
    //             .thenThrow(new RuntimeException("QuizCollection not found with id: " + id));
                
    //     System.out.println("Đang gọi controller.updateQuizCollection với ID không tồn tại");
    //     ResponseEntity<QuizCollection> response = controller.updateQuizCollection(id, dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là NOT_FOUND");
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: updateQuizCollection_NotFound =====");
    // }
    
    // @Test
    // @DisplayName("Kiểm tra cập nhật QuizCollection thất bại do lỗi IO")
    // void updateQuizCollection_IOError() throws IOException {
    //     System.out.println("===== BẮT ĐẦU TEST: updateQuizCollection_IOError =====");
        
    //     Long id = 1L;
    //     QuizCollectionDTO dto = TestDataFactory.createQuizCollectionDTOForUpdateWithImage(id);
        
    //     when(quizCollectionService.updateQuizCollection(eq(id), any(QuizCollectionDTO.class), any(MultipartFile.class)))
    //             .thenThrow(new IOException("Lỗi khi cập nhật file"));
                
    //     System.out.println("Đang gọi controller.updateQuizCollection với dữ liệu gây lỗi IO");
    //     ResponseEntity<QuizCollection> response = controller.updateQuizCollection(id, dto, request);
        
    //     System.out.println("Kiểm tra kết quả trả về phải là INTERNAL_SERVER_ERROR");
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
    //     System.out.println("===== KẾT THÚC TEST: updateQuizCollection_IOError =====");
    // }

    private QuizCollection createMockCollection(Long id) {
        QuizCollection collection = new QuizCollection();
        collection.setId(id);
        collection.setAuthorId(id);
        collection.setCategory("Collection " + id);
        collection.setDescription("Description " + id);
        collection.setVisibleTo(true);
        collection.setCoverPhoto("quiz-collections/" + id + "/cover.jpg");
        return collection;
    }
}