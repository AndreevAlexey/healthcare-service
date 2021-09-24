package ru.netology.patient.service.medical;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.*;
import ru.netology.patient.service.alert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MedicalServiceMock {
    private MedicalService medicalService;
    private PatientInfoRepository patientInfoRepository;
    private SendAlertService alertService;
    private PatientInfo patient;

    @BeforeEach
    public void init() {
        patientInfoRepository = mock(PatientInfoFileRepository.class);
        alertService = mock(SendAlertServiceImpl.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        patient = new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
    }
    @Test
    void checkBloodPressure_test_EXISTS_MESSAGE() {
        // given
        BloodPressure currentPressure = new BloodPressure(60, 120);
        when(patientInfoRepository.getById(Mockito.any())).thenReturn(patient);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        // when
        medicalService.checkBloodPressure("111", currentPressure);
        // then
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertNotNull(argumentCaptor.getValue());
    }
    @Test
    void checkBloodPressure_test_NULL_MESSAGE() {
        // given
        BloodPressure currentPressure = new BloodPressure(120, 80);
        when(patientInfoRepository.getById(Mockito.any())).thenReturn(patient);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        // when
        medicalService.checkBloodPressure("111", currentPressure);
        // then
        Mockito.verify(alertService, Mockito.times(0)).send(Mockito.any());
    }
    @Test
    void checkTemperature_test_EXISTS_MESSAGE() {
        // given
        BigDecimal currentTemperature = new BigDecimal("32.0");
        when(patientInfoRepository.getById(Mockito.any())).thenReturn(patient);
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        // when
        medicalService.checkTemperature("111", currentTemperature);
        // then
        Mockito.verify(alertService).send(argumentCaptor2.capture());
        Assertions.assertNotNull(argumentCaptor2.getValue());
    }
    @Test
    void checkTemperature_test_NULL_MESSAGE() {
        // given
        BigDecimal currentTemperature = new BigDecimal("36.7");
        when(patientInfoRepository.getById(Mockito.any())).thenReturn(patient);
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
        // when
        medicalService.checkTemperature("111", currentTemperature);
        // then
        Mockito.verify(alertService, Mockito.times(0)).send(Mockito.any());
    }
}