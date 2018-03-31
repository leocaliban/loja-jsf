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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.leocaliban.lojajsf.service.NegocioException;

public class JsfExceptionHandler extends ExceptionHandlerWrapper{

	private static Log log = LogFactory.getLog(JsfExceptionHandler.class);
	
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
			NegocioException negocioException = getNegocioException(exception);
			
			boolean handled = false;
			try {
				if(exception instanceof ViewExpiredException) {
					handled = true;
					redirecionar("/");
				}
				else if(negocioException != null) {
					handled = true;
					//log - mensagem e causa da exceção
					log.error("Erro de sistema: " + exception.getMessage(), exception);
					FacesUtil.adicionarMensagemErro(negocioException.getMessage());
				}
				else {
					handled = true;
					redirecionar("/erro.xhtml");
				}
			}
			finally {
				if(handled) {
					eventos.remove();
				}
			}
		}
		getWrapped().handle();
	}

	//varre a pilha de exceções procurando NegocioException com recursividade
	private NegocioException getNegocioException(Throwable exception) {
		if(exception instanceof NegocioException) {
			return (NegocioException) exception;
		}
		else if(exception.getCause() != null) {
			return getNegocioException(exception.getCause());
		}
		return null;
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
