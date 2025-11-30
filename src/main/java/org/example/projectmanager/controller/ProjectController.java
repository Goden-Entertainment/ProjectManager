package org.example.projectmanager.controller;

import jakarta.websocket.Session;
import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProjectController {
    private ProjectService projectService;

    @GetMapping("/addNewSubProject")
    public String addNewSubProject(Model model, Session session) {
        SubProject subProject = new SubProject();
        model.addAttribute("newSubProject", subProject);
        return "addNewSubProjectForm";
    }

    @PostMapping("/addNewSubProject")
    public String addNewSubProject(@ModelAttribute SubProject subProject) {
        projectService.createSubProject(subProject);
        return "redirect:/profile";
    }


    @GetMapping("/editSubProject/{subProjectId}")
    public String editSubProject(@PathVariable int subProjectId, Model model, Session session) {
        SubProject subProject = projectService.findSubProject(subProjectId);
        model.addAttribute("subProject", subProject);

        return "editSubProjectForm";

    }

    @PostMapping("/editSubProject")
    public String editSubproject(@ModelAttribute SubProject subProject) {
        projectService.editSubProject(subProject);
        return "redirect:/profile";
    }

//    @GetMapping("/deleteSubProject/{subProjectId}")
//    public String deleteSubproject(@PathVariable int subProjectId) {
//        projectService.deleteSubProject(subProjectId);
//        return "redirect:/profile";
//
//    }

    @PostMapping("/deleteSubProject/{subProjectId}")
    public String deleteSubProject(@PathVariable int subProjectId) {
        projectService.deleteSubProject(subProjectId);
        return "redirect:/profile";
    }

    @GetMapping("/mySubProject")
    public String mySubProject(Model model, @PathVariable int subProjectId){
        //Jeg skal have vist alle de "sub" projekter for en medarbejder, så subprojekter skal tilknyttes
        //Og de skal vises via denne metode
        return "profile";
    }


}
