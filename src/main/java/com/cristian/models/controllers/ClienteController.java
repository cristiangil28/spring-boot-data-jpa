package com.cristian.models.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cristian.models.entity.Cliente;
import com.cristian.paginator.PageRender;
import com.cristian.service.IClienteService;

@Controller
@SessionAttributes("cliente")//mantiene la sesion del cliente con sus datos
public class ClienteController {

	@Autowired
	
	private IClienteService clienteService;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page,Model model) {
		
		Pageable pageRequest=PageRequest.of(page,6);
		Page<Cliente> clientes=clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender= new PageRender<>("/listar",clientes);
		model.addAttribute("lista", "listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	@RequestMapping("/form")
	public String crear(Map<String,Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo","Formulario de Cliente");
		
		return "form";
	}
	/*
	 * @valid habilita la validacion en el objeto mapeado en el Form
	 * BindingResult valida si hay errores en la consulta
	 */
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult res,Model model,RedirectAttributes flash ,SessionStatus status) {
		
		if(res.hasErrors()) {
			model.addAttribute("titulo","Formulario de Cliente");
			return "form";
		}
		String messageFlash=(cliente.getId()!=null)?"Cliente editado con éxito":"cliente creado con éxito";
		clienteService.save(cliente);
		status.setComplete();//se sierra la sesion del cliente
		flash.addFlashAttribute("success",messageFlash);
		return "redirect:listar";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model,RedirectAttributes flash) {
		Cliente cliente=null;
		if(id>0) {
			
			cliente=clienteService.findOne(id);
			
			if(cliente==null) {
				flash.addFlashAttribute("error","el ID del cliente no existe en la BD");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error","el ID del cliente no puede ser cero");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes flash) {
		if(id>0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success","cliente eliminado con éxito");
		}
		return "redirect:/listar";
	}
}
