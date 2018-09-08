/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.edu.eg.jdproject.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import co.edu.eg.jdproject.data.MemberRepository;
import co.edu.eg.jdproject.model.Member;
import co.edu.eg.jdproject.service.MemberRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6

@Model
public class RegistroPrimeController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private MemberRegistration memberRegistration;
    
    @Inject
    private MemberRepository memberRepo;

    
    private Member newMember;
    
    private List<Member> memberList;

    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
        consultarTodos();
    }
    
    /**
     * Metodo de consulta de lista
     * @throws Exception
     */
    public void consultarTodos() {
    	memberList = memberRepo.findAllOrderedByName();
    	
    }
    
    
    public void register() throws Exception {
        try {
        	if (newMember != null && newMember.getId() != null) {
        		update();
        		return;
				
			}
        	if(newMember==null) {
        		FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error!", "error inesperado");
                facesContext.addMessage(null, m);
                return;
        	}
            memberRegistration.register(newMember);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
            initNewMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    /**
     * Actualizar registro
     * @throws Exception
     */
    public void update() throws Exception {
        try {
        	if (newMember != null && newMember.getId()!=null) {
        		
        		 FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
        				 "error!", "Error al registrar");
                 facesContext.addMessage(null, m);
                 return;
			}
            memberRegistration.actualizar(newMember);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado!", "Actualización realizada");
            facesContext.addMessage(null, m);
            
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Error al registrar");
            facesContext.addMessage(null, m);
        }
    }

    /**
     * metodo que llama a la funcion borrar
     * @throws Exception
     */
    public void remove(long id) throws Exception {
        try {
            memberRegistration.borrar(id); /**instancia de la clase memberregistracion **/
            consultarTodos();
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado", "Eliminación exitosa");
            facesContext.addMessage(null, m);
            
            
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Error eliminando");
            facesContext.addMessage(null, m);
        }
    }
    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

	public List<Member> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}

	public Member getNewMember() {
		return newMember;
	}

	public void setNewMember(Member newMember) {
		this.newMember = newMember;
	}
	public void verMember(Member newMember) {
		this.newMember = newMember;
	}

}
