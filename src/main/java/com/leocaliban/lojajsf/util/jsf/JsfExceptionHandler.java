package com.leocaliban.lojajsf.util.jsf;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

public class JsfExceptionHandler extends ExceptionHandlerWrapper{

	private ExceptionHandler wrapped;
	
	public JsfExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}
	
	@Override
	/**
	 * Empacota o tratador de exceções.
	 */
	public ExceptionHandler getWrapped() {
		
		return this.wrapped;
	}

	@Override
	/**
	 * Trata as exceções capturadas pelo jsf.
	 */
	public void handle() throws FacesException {
		Iterator<ExceptionQueuedEvent> eventos = getUnhandledExceptionQueuedEvents().iterator();
		while (eventos.hasNext()) {
			ExceptionQueuedEvent evento = eventos.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) evento.getSource();
			
			Throwable exception = context.getException();
			
			try {
				if(exception instanceof ViewExpiredException) {
					redirecionar("/");
				}
			}
			finally {
				eventos.remove();
			}
		}
		getWrapped().handle();
	}

	private void redirecionar(String pagina) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			String contextPath = externalContext.getRequestContextPath();
			
			externalContext.redirect(contextPath + pagina);
			
			facesContext.responseComplete();
		}
		catch (IOException e) {
			throw new FacesException(e);
		}
	}
}
