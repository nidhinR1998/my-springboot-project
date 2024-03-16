package com.springbootfproject.firstWebApp.todo;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("name")
public class TodoController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private TodoService todoService;

	public TodoController(TodoService todoService) {
		super();
		this.todoService = todoService;
	}

	@RequestMapping("list-todos")
	private String listAllTodos(ModelMap model) {
		String username = getLoggedInUsername(model);
		List<Todo> todo = todoService.findByUsername(username);
		logger.debug("List All Todo Hit");
		model.addAttribute("todos", todo);
		return "listTodos";
	}

	
	@RequestMapping(value = "add-todo", method = RequestMethod.GET)
	private String showNewTodoPage(ModelMap model) {
		String username = getLoggedInUsername(model);
		Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);
		model.put("todo", todo);
		logger.debug("Show New Todo Page Hit");
		return "todo";
	}

	@RequestMapping(value = "add-todo", method = RequestMethod.POST)
	private String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		if (result.hasErrors()) {

			return "todo";
		}
		String username = getLoggedInUsername(model);
		todoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), false);
		logger.debug("Add New Todo Hit");
		return "redirect:list-todos";
	}

	@RequestMapping("delete-todo")
	private String deleteTodo(@RequestParam int id) {
		todoService.deleteById(id);
		logger.debug("Delete Todo Hit");
		return "redirect:list-todos";
	}

	@RequestMapping(value = "update-todo", method = RequestMethod.GET)
	private String showUpdateTodo(@RequestParam int id, ModelMap model) {
		Todo todo = todoService.findById(id);
		model.addAttribute("todo", todo);
		logger.debug("Show Update Todo Hit");
		return "todo";
	}

	@RequestMapping(value = "update-todo", method = RequestMethod.POST)
	private String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		if (result.hasErrors()) {

			return "todo";
		}
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoService.updateTodo(todo);
		logger.debug("Update Todo Hit");
		return "redirect:list-todos";
	}
	
	private String getLoggedInUsername(ModelMap model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

}
