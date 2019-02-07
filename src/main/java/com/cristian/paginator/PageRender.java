package com.cristian.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	private String url;
	private Page<T> page;
	private int totalPage;
	private int paginaActual;
	private int numElementosPagina;
	private List<PageItem> paginas;
	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		paginas =new ArrayList<PageItem>();
		numElementosPagina=page.getSize();
		totalPage=page.getTotalPages();
		paginaActual=page.getNumber()+1;
		
		int desde,hasta;
		if(totalPage<=numElementosPagina) {
			desde=1;
			hasta=totalPage;
		}else {
			if(paginaActual<=numElementosPagina/2) {
				desde=1;
				hasta=numElementosPagina;
			}else if(paginaActual>=totalPage-numElementosPagina/2) {
				desde=totalPage-numElementosPagina+1;
				hasta=numElementosPagina;
			}else {
				desde=paginaActual-numElementosPagina/2;
				hasta=numElementosPagina;
			}
		}
		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde+1, paginaActual==desde+i));
		}
	}
	public String getUrl() {
		return url;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public int getPaginaActual() {
		return paginaActual;
	}
	public List<PageItem> getPaginas() {
		return paginas;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}
