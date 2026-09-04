package com.mycompany.sunrisedentalclinic.service;

import com.mycompany.sunrisedentalclinic.dao.AppointmentDAO;
import com.mycompany.sunrisedentalclinic.model.Appointment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentService {

    private final AppointmentDAO appointmentDAO;

    public AppointmentService() {
        appointmentDAO = new AppointmentDAO();
    }

    @GET
    public Response getAllAppointments() {
        try {
            List<Appointment> appointments =
                    appointmentDAO.getAllAppointments();

            return Response.ok(appointments).build();

        } catch (Exception e) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR
            ).entity(
                    "Unable to retrieve appointments."
            ).build();
        }
    }

    @GET
    @Path("/{appointmentNumber}")
    public Response getAppointment(
            @PathParam("appointmentNumber") String appointmentNumber) {

        if (appointmentNumber == null ||
                appointmentNumber.trim().isEmpty()) {

            return Response.status(
                    Response.Status.BAD_REQUEST
            ).entity(
                    "Appointment number is required."
            ).build();
        }

        try {
            Appointment appointment =
                    appointmentDAO.getAppointmentByNumber(
                            appointmentNumber.trim()
                    );

            if (appointment == null) {
                return Response.status(
                        Response.Status.NOT_FOUND
                ).entity(
                        "Appointment not found."
                ).build();
            }

            return Response.ok(appointment).build();

        } catch (Exception e) {
            return Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR
            ).entity(
                    "Unable to retrieve appointment."
            ).build();
        }
    }
}
