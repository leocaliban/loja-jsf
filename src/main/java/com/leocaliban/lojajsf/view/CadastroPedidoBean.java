package com.leocaliban.lojajsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.leocaliban.lojajsf.service.NegocioException;

@ManagedBean
@RequestScoped
public class CadastroPedidoBean{

	private List<Integer> itens;
	
	public CadastroPedidoBean() {
		itens = new ArrayList<>();
		itens.add(1);
	}

	public List<Integer> getItens() {
		return itens;
	}
	
	public void salvar() {
		throw new NegocioException("Pedido não pode ser salvo, porque ainda não foi implementado");
	}
}
