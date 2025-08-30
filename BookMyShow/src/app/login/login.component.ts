import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup,FormControl, Validators  } from '@angular/forms';
import { LoginService } from '../services/loginService/login.service';
import {  Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserserviceService } from '../services/user/userservice.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  ngOnInit() {
  const token = sessionStorage.getItem('access_token');
  if (token!=='undefined' && token!=null) {
    this.router.navigate(['/base/home']);
  }
  }

  constructor(private loginService : LoginService, private router : Router, private userService:UserserviceService){}

  apiError?:string;

   loginForm = new FormGroup({
    username : new FormControl('', [Validators.required]),
    password : new FormControl('', [Validators.required])
  });
  
  public onLogin() : void{

    let username  = this.loginForm.get("username")?.value;
    let password  = this.loginForm.get("password")?.value;

    if(username && password){
      this.loginService.login(username, password).subscribe(
        {
          next :response => {
            console.log("Logged in Successfully");
            sessionStorage.setItem('access_token', response.accessToken); 

            this.userService.LoadUserData().subscribe({
              next:user => {
                console.log("User set");
                this.router.navigate(['/base/home']);
              }
            })   
          },
          error : error => {
            if(error.status == 0 ){
              this.apiError = "Unable to connect to backend servers"
            }else{
              this.apiError = "Login Failed. Username or password in invalid";
            }
          }
        }
      )
    }else{
      console.log("Please check username or password");
    }

  }


}
