package com.mapt.resources;

import com.mapt.core.User;
import com.mapt.db.UserDAO;

import io.dropwizard.hibernate.UnitOfWork;

import com.codahale.metrics.annotation.Timed;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/pdf")
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class PDFResource
{
	private String pdfPath = "";
	
    public PDFResource(String pdfPath)
    {
    	this.pdfPath = pdfPath;
    }

    @GET
    @Timed
    @UnitOfWork 
    @RolesAllowed({"ADMIN", "VIP"})
    public Response getPdf(@Context SecurityContext context)
    {
    	User currentUser = (User) context.getUserPrincipal();
    	
    	StreamingOutput fileStream =  new StreamingOutput()
        {
            @Override
            public void write(java.io.OutputStream output) throws IOException
            {
                    File pdf = new File(pdfPath + currentUser.getUsername() + "-resume.pdf");
                    byte[] data = Files.readAllBytes(pdf.toPath());
                    
                    output.write(data);
                    output.flush();
            }
        };
        
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition","attachment;filename =" + currentUser.getUsername() + "-resume.pdf")
                .build();
    }
}
