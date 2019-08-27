package com.redhat.loja.service;

import java.util.List;

public interface CrudServices<T> {
	List<T> listarTodos();
}
