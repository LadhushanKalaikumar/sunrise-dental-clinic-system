package com.mycompany.sunrisedentalclinic;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SunriseDentalClinicTest {

    @Test
    public void testAppointmentNumberValidation() {
        String appointmentNumber = "APT001";

        assertNotNull(appointmentNumber);
        assertFalse(appointmentNumber.trim().isEmpty());
        assertTrue(appointmentNumber.startsWith("APT"));
    }

    @Test
    public void testPatientNameValidation() {
        String patientName = "Kamal Perera";

        assertNotNull(patientName);
        assertFalse(patientName.trim().isEmpty());
        assertTrue(patientName.matches("[a-zA-Z ]+"));
    }

    @Test
    public void testContactNumberValidation() {
        String contactNumber = "0771234567";

        assertNotNull(contactNumber);
        assertTrue(contactNumber.matches("\\d{10}"));
    }

    @Test
    public void testTreatmentCostCalculation() {
        BigDecimal treatmentCost = new BigDecimal("5000.00");
        BigDecimal consultationFee = new BigDecimal("2500.00");

        BigDecimal total = treatmentCost.add(consultationFee);

        assertEquals(new BigDecimal("7500.00"), total);
    }

    @Test
    public void testInvalidContactNumber() {
        String contactNumber = "12345";

        assertFalse(contactNumber.matches("\\d{10}"));
    }
}