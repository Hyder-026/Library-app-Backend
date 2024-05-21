package com.reactspringboot.libraryapp.controller;

import com.reactspringboot.libraryapp.requestmodels.AddBookRequest;
import com.reactspringboot.libraryapp.service.AdminService;
import com.reactspringboot.libraryapp.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(@RequestHeader("Authorization") String token,
                                     @RequestParam Long bookId) throws Exception {
        boolean admin = ExtractJWT.payloadJWTExtraction(token,"\"userType\"").equalsIgnoreCase("admin");
        if(!admin) {
            throw new Exception("Administration access only.");
        }
        adminService.increaseBookQuantity(bookId);
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@RequestHeader("Authorization") String token,
                                     @RequestParam Long bookId) throws Exception {
        boolean admin = ExtractJWT.payloadJWTExtraction(token,"\"userType\"").equalsIgnoreCase("admin");
        if(!admin) {
            throw new Exception("Administration access only.");
        }
        adminService.decreaseBookQuantity(bookId);
    }

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value = "Authorization") String token,
                         @RequestBody AddBookRequest addBookRequest) throws Exception {
        boolean isAdmin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"").equalsIgnoreCase("admin");
        if(!isAdmin) {
            throw new Exception("Administration access only.");
        }
        adminService.postBook(addBookRequest);
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader("Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        boolean isAdmin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"").equalsIgnoreCase("admin");
        if(!isAdmin) {
            throw new Exception("Administration access only.");
        }
        adminService.deleteBook(bookId);
    }
}
