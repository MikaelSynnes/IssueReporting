/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import Data.Report;
import Data.Token;
import Data.UserDto;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Model;

import javax.ws.rs.GET;

import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Mikael
 */
@RestController
public class API {
   private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
 
    private HashMap<String, Token> accessTokens;
       private ArrayList<Report> tickets;

  
    @Autowired
    public API(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
       this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        tickets = createDummyTickets();
    }

 @RequestMapping("/add")
    public String add(@RequestParam("username") String username, @RequestParam("password") String password) {
        UserDetails user=  User.withDefaultPasswordEncoder().username(username).password(password).roles("USER").build();
        inMemoryUserDetailsManager.createUser(user);
      
        return new RedirectView("/");
    }

    

      /*  String stringbuilder = null;    
        for(Report report:createDummyTickets()){
            stringbuilder+="User: " + report.getUser()+ "Report: " + report.getIssue() + "      ";
        }
        return stringbuilder;   
*/
    
@RequestMapping("/tickets")
public ModelAndView adminPage() {
    ModelAndView model = new ModelAndView();
 
    model.addObject("tickets",tickets);

    return model;

}

@RequestMapping("/createticket/addticket")
    public RedirectView addTicket(@RequestParam("user") String user,@RequestParam("text") String text){
        tickets.add(new Report(text,user));
        return new RedirectView("/");
    }
    




    @GET
    @Path("test")
    public String test() {
        return "This is a test function";
    }

   
    private ArrayList<Report> createDummyTickets() {
        ArrayList<Report> reports = new ArrayList();
        reports.add(new Report("This is a dummy issue", ""));
        reports.add(new Report("This is a dummy issue 2", ""));
        reports.add(new Report("This is a dummy issue 3", "I chose to show name"));
        reports.add(new Report("The ticket system is trash", "Mikael"));

        return reports;
    }

  

  
}
