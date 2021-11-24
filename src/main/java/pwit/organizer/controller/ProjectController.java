package pwit.organizer.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pwit.organizer.logic.ProjectService;
import pwit.organizer.model.Project;
import pwit.organizer.model.ProjectStep;
import pwit.organizer.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model){
        ProjectWriteModel projectWriteModel = new ProjectWriteModel();
        projectWriteModel.setDescription("testowy");
        model.addAttribute("project", projectWriteModel);
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "projects";
        }
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano Project !");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline){

        try {
            service.createGroup(id, deadline);
            model.addAttribute("message", "Dodano grupe");
        } catch (IllegalStateException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
       return service.readAll();
    }


}
