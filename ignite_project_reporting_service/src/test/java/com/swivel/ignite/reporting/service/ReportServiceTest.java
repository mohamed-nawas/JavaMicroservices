package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.StudentResponseDto;
import com.swivel.ignite.reporting.dto.response.StudentsIdListResponseDto;
import com.swivel.ignite.reporting.dto.response.TuitionListResponseDto;
import com.swivel.ignite.reporting.dto.response.TuitionResponseDto;
import com.swivel.ignite.reporting.entity.Report;
import com.swivel.ignite.reporting.enums.Month;
import com.swivel.ignite.reporting.exception.ReportNotFoundException;
import com.swivel.ignite.reporting.exception.ReportingServiceException;
import com.swivel.ignite.reporting.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link ReportService} class
 */
class ReportServiceTest {

    private static final String STUDENT_ID = "sid-123456789";
    private static final String STUDENT_1_ID = "sid-987654321";
    private static final String TUITION_ID = "tid-123456789";
    private static final String REPORT_ID = "rid-123456789";
    private static final String TOKEN = "Bearer 123456789";
    private static final String ERROR = "ERROR";
    private ReportService reportService;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private StudentService studentService;
    @Mock
    private TuitionService tuitionService;
    @Mock
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        reportService = new ReportService(reportRepository, studentService, tuitionService, paymentService);
    }

    /**
     * Start of tests for updateReport method
     */
    @Test
    void Should_UpdateReport() throws IOException {
        doNothing().when(reportRepository).deleteAll();
        when(tuitionService.getTuitionList(anyString())).thenReturn(getSampleTuitionListResponseDto());
        when(paymentService.getPaidStudents(anyString(), anyString(), anyString()))
                .thenReturn(getSampleStudents1IdListResponseDto());
        when(studentService.getStudentInfo(anyString(), anyString())).thenReturn(getSampleStudentResponseDto());
        reportService.updateReport(TOKEN);
        verify(reportRepository, times(33)).save(any(Report.class));
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForErrorCreatingPaidReportList() throws IOException {
        doNothing().when(reportRepository).deleteAll();
        when(tuitionService.getTuitionList(anyString())).thenReturn(getSampleTuitionListResponseDto());
        doThrow(new DataAccessException(ERROR) {
        }).when(reportRepository).save(any(Report.class));
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport(TOKEN));
        assertEquals("Error creating paid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForErrorCreatingUnPaidReportList() throws IOException {
        Report r = new Report();
        doNothing().when(reportRepository).deleteAll();
        when(tuitionService.getTuitionList(anyString())).thenReturn(getSampleTuitionListResponseDto());
        when(reportRepository.save(any(Report.class))).thenReturn(r, r, r, r, r, r, r, r, r, r, r, r)
                .thenThrow(new DataAccessException(ERROR) {
                });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport(TOKEN));
        assertEquals("Error creating unpaid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForFailedToUpdatePaidReportList() throws IOException {
        Report r = new Report();
        doNothing().when(reportRepository).deleteAll();
        when(tuitionService.getTuitionList(anyString())).thenReturn(getSampleTuitionListResponseDto());
        when(paymentService.getPaidStudents(anyString(), anyString(), anyString()))
                .thenReturn(getSampleStudentsIdListResponseDto());
        when(reportRepository.save(any(Report.class))).thenReturn(r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r,
                r, r, r, r, r, r).thenThrow(new DataAccessException(ERROR) {
        });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport(TOKEN));
        assertEquals("Failed to update paid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForFailedToGetTuitionJoinedMonthOfStudent()
            throws IOException {
        doNothing().when(reportRepository).deleteAll();
        when(tuitionService.getTuitionList(anyString())).thenReturn(getSampleTuitionListResponseDto());
        when(paymentService.getPaidStudents(anyString(), anyString(), anyString()))
                .thenReturn(getSampleStudents1IdListResponseDto());
        when(studentService.getStudentInfo(anyString(), anyString())).thenThrow(new IOException());
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport(TOKEN));
        assertEquals("Failed to get tuition joined month of student for id: " + STUDENT_ID,
                exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForFailedToUpdateUnPaidReportList() throws IOException {
        Report r = new Report();
        doNothing().when(reportRepository).deleteAll();
        when(tuitionService.getTuitionList(anyString())).thenReturn(getSampleTuitionListResponseDto());
        when(paymentService.getPaidStudents(anyString(), anyString(), anyString()))
                .thenReturn(getSampleStudentsIdListResponseDto());
        when(reportRepository.save(any(Report.class))).thenReturn(r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r,
                r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r).thenThrow(new DataAccessException(ERROR) {
        });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport(TOKEN));
        assertEquals("Failed to update unpaid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportIsFailed() {
        doThrow(new DataAccessException(ERROR) {
        }).when(reportRepository).deleteAll();
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport(TOKEN));
        assertEquals("Failed to update report", exception.getMessage());
    }

    /**
     * Start of tests for getByTuitionIdMonthPaid method
     */
    @Test
    void Should_ReturnReport_When_GettingByTuitionIdMonthPaidIsSuccessful() {
        when(reportRepository.findByTuitionIdAndMonthAndIsPaid(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(getSampleReport()));
        assertEquals(REPORT_ID, reportService.getByTuitionIdMonthPaid(TUITION_ID, Month.MARCH.getMonthString(),
                true).getId());
    }

    @Test
    void Should_ThrowReportNotFoundException_When_GettingByTuitionIdMonthPaidForReportNotFound() {
        String month = Month.MAY.getMonthString();
        when(reportRepository.findByTuitionIdAndMonthAndIsPaid(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());
        ReportNotFoundException exception = assertThrows(ReportNotFoundException.class, () ->
                reportService.getByTuitionIdMonthPaid(TUITION_ID, month, true));
        assertEquals("Report not found for getting by tuitionId, month, isPaid", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_GettingByTuitionIdMonthPaidIsFailed() {
        String month = Month.MAY.getMonthString();
        when(reportRepository.findByTuitionIdAndMonthAndIsPaid(anyString(), anyString(), anyBoolean()))
                .thenThrow(new DataAccessException(ERROR) {
                });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.getByTuitionIdMonthPaid(TUITION_ID, month, true));
        assertEquals("Failed to get report by tuitionId and month", exception.getMessage());
    }

    /**
     * This method returns a sample TuitionListResponseDto
     *
     * @return TuitionListResponseDto
     */
    private TuitionListResponseDto getSampleTuitionListResponseDto() {
        TuitionListResponseDto dto = new TuitionListResponseDto();
        dto.getTuitionList().add(getSampleTuitionResponseDto());
        return dto;
    }

    /**
     * This method returns a sample TuitionResponseDto
     *
     * @return TuitionResponseDto
     */
    private TuitionResponseDto getSampleTuitionResponseDto() {
        TuitionResponseDto dto = new TuitionResponseDto();
        dto.setTuitionId(TUITION_ID);
        dto.getStudentIds().add(STUDENT_ID);
        return dto;
    }

    /**
     * This method returns a sample StudentsIdListResponseDto
     *
     * @return StudentsIdListResponseDto
     */
    private StudentsIdListResponseDto getSampleStudentsIdListResponseDto() {
        List<String> studentIds = new ArrayList<>();
        studentIds.add(STUDENT_ID);
        return new StudentsIdListResponseDto(studentIds);
    }

    /**
     * This method returns a sample StudentsIdListResponseDto
     *
     * @return StudentsIdListResponseDto
     */
    private StudentsIdListResponseDto getSampleStudents1IdListResponseDto() {
        List<String> studentIds = new ArrayList<>();
        studentIds.add(STUDENT_1_ID);
        return new StudentsIdListResponseDto(studentIds);
    }

    /**
     * This method returns a sample Report
     *
     * @return Report
     */
    private Report getSampleReport() {
        Report report = new Report();
        report.setId(REPORT_ID);
        return report;
    }

    /**
     * This method returns a sample StudentResponseDto
     *
     * @return StudentResponseDto
     */
    private StudentResponseDto getSampleStudentResponseDto() {
        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setTuitionJoinedOn(new Date(1680497462842L));
        return responseDto;
    }
}
