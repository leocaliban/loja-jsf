package com.leocaliban.lojajsf;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.leocaliban.lojajsf.model.Cliente;
import com.leocaliban.lojajsf.model.Endereco;
import com.leocaliban.lojajsf.model.TipoPessoa;

public class Teste {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
		
		EntityManager manager = factory.createEntityManager();
		
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		
		Cliente cliente = new Cliente();
		cliente.setNome("Leonardo Batista");
		cliente.setEmail("leocaliban@gmail.com");
		cliente.setDocumento("091.680.305-20");
		cliente.setTipo(TipoPessoa.FISICA);
		
		Cliente cliente2 = new Cliente();
		cliente2.setNome("Ronaldo Oliveira");
		cliente2.setEmail("ronan@gmail.com");
		cliente2.setDocumento("900.665.955-80");
		cliente2.setTipo(TipoPessoa.FISICA);
		
		Endereco endereco = new Endereco();
		endereco.setRua("Rua nova");
		endereco.setNumero("200");
		endereco.setCidade("São Paulo");
		endereco.setEstado("SP");
		endereco.setCep("58707-420");
		
		endereco.setCliente(cliente);
		
		cliente.getEnderecos().add(endereco);
		cliente2.getEnderecos().add(endereco);
		
		manager.persist(cliente);
		manager.persist(cliente2);
		
		transaction.commit();
		
		
	}
}
