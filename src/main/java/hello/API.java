/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import Data.Report;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Mikael
 */
@RestController
public class API {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private ArrayList<Report> tickets;
    private ArrayList<Report> resolved;

    @Autowired
    public API(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        tickets = createDummyTickets();
        resolved=new ArrayList<Report>();
    }

    @RequestMapping("/add")
    public RedirectView add(@RequestParam("username") String username, @RequestParam("password") String password) {
        try{
        UserDetails user = User.withDefaultPasswordEncoder().username(username).password(password).roles("USER").build();
        inMemoryUserDetailsManager.createUser(user);

        return new RedirectView("/");
        }catch(Exception e){
            return new RedirectView("/registration");
        }
    }
    
    @RequestMapping("tickets/remove{id}")
    public RedirectView remove(@PathVariable String id) {
        Report ti = null;
        for (Report t : tickets) {
            if (t.getId().equals(id)) {
                ti = t;

            }
        }
        resolved.add(ti);
        tickets.remove(ti);
        
        return new RedirectView("/tickets");

    }

    @RequestMapping("/tickets")
    public ModelAndView adminPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("tickets", tickets);
        return model;
    }

    @RequestMapping("/resolved")
    public ModelAndView resolvedTickets() {
        ModelAndView model = new ModelAndView();
        model.addObject("resolved", resolved);
        return model;
    }

    @RequestMapping("/createticket/addticket")
    public RedirectView addTicket(@RequestParam("user") String user, @RequestParam("text") String text) {
        String id = UUID.randomUUID().toString();
        tickets.add(new Report(text, user, id));
        return new RedirectView("/");
    }

    private ArrayList<Report> createDummyTickets() {
        ArrayList<Report> reports = new ArrayList();
        reports.add(new Report("This is a dummy issue", "Anonymous", UUID.randomUUID().toString()));
        reports.add(new Report("This is a dummy issue 2", "Anonymous", UUID.randomUUID().toString()));
        reports.add(new Report("This is a dummy issue 3", "I chose to show name", UUID.randomUUID().toString()));
        reports.add(new Report("The ticket system is trash", "Mikael", UUID.randomUUID().toString()));
        return reports;
    }

}
