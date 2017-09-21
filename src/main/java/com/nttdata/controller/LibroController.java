package com.nttdata.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.database.LibroMapper;
import com.nttdata.exception.BadRequestException;
import com.nttdata.exception.NoContentException;
import com.nttdata.exception.ResourceConflictException;
import com.nttdata.exception.ResourceNotFoundException;
import com.nttdata.model.Libro;



@RestController
public class LibroController {

	@Autowired
	private LibroMapper libroMapper;

	@RequestMapping(method = RequestMethod.GET, value = "/libro")
	public List<Libro> listLibr(@RequestParam(value="genere", required= false) String genere,
			@RequestParam(value="titolo", required= false) String titolo,
			@RequestParam(value="prezzogt", required= false) BigDecimal prezzogt,
			@RequestParam(value="prezzolt", required= false) BigDecimal prezzolt) {
	
		Libro params = new Libro();
		params.setGenere(genere);
		params.setTitolo(titolo);
		List<Libro> findAll = libroMapper.findAll(params);
		
		List<Libro> libriFiltred = new ArrayList<>();
	
		for(Libro l : findAll) {
			if(prezzogt != null) {
				if(l.getPrezzo().compareTo(prezzogt)>=0) {
					libriFiltred.add(l);					
				}
		}
			else {
				libriFiltred.add(l);
			}
		}	
		
		if (libriFiltred != null && libriFiltred.isEmpty())
			throw new ResourceNotFoundException();
		return libriFiltred;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/libro/{idLibro}")
	public Libro get(@PathVariable(value = "idLibro", required = true) int idLibro) {
		Libro libro = libroMapper.findByIdLibro(idLibro);
		if (libro != null)
			return libro;
		else
			throw new ResourceNotFoundException();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/libro")
	public Libro add(@RequestBody Libro libro) {
		
	    if(!validateLibro(libro)){
	        throw new BadRequestException();
	        }

		
		Libro foundLibro = libroMapper.findByIdLibro(libro.getIdLibro());
		if (foundLibro != null)
			throw new ResourceConflictException();

		libroMapper.add(libro);
		return libro;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/libro/{idLibro}")
	public Libro update(@RequestBody Libro libro, @PathVariable(value = "idLibro", required = true) int idLibro) {
		
		 if(!validateLibro(libro)){
		        throw new BadRequestException();
		        }
		
		Libro foundLibro = libroMapper.findByIdLibro(idLibro);
		if (foundLibro == null)
			throw new NoContentException();

		libro.setIdLibro(idLibro);
		libroMapper.update(libro);
		return libro;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/libro/{idLibro}")
	public void delete(@PathVariable(value = "idLibro", required = true) int idLibro) {
		Libro foundLibro = libroMapper.findByIdLibro(idLibro);
		if (foundLibro == null)
			throw new NoContentException();
		libroMapper.delete(idLibro);
	}
	
private boolean validateLibro(Libro libro) {
    	
		if(StringUtils.isBlank(libro.getTitolo()))
			return false;
		if(StringUtils.isBlank(libro.getGenere()))
			return false;
		if(libro.getPrezzo()==null)
			return false;
		if(StringUtils.isBlank(libro.getScaffale()))
			return false;
		
		return true;
	}
}



