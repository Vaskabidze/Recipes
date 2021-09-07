package recipes.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.UserService;
import recipes.model.User;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Save a new user to the DB
     * return HttpStatus.OK
     * if the user is already in the database - return HttpStatus.BAD_REQUEST
     *
     * @param user JSON with New Recipe
     */
    @PostMapping("/register")
    public void newUser(@Valid @RequestBody User user) {
        userService.saveUser(user);
        throw new ResponseStatusException(HttpStatus.OK);
    }

    /**
     * Return user by ID. Only Admin permission
     * return HttpStatus.NO_CONTENT
     * if the user is not in the database - return HttpStatus.NOT_FOUND
     *
     * @param id User ID
     */
    @DeleteMapping("/deleteuser/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }


}
