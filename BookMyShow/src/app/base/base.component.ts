import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { LoginService } from '../services/loginService/login.service';
import { BaseNavComponent } from "../base-nav/base-nav.component";

@Component({
  selector: 'app-base',
  imports: [ RouterModule],
  templateUrl: './base.component.html',
  styleUrl: './base.component.css'
})
export class BaseComponent {

  constructor(private loginService : LoginService, private router:Router){}

  logout():void{
    this.loginService.logout().subscribe({
      next:response =>{
        console.log("Logged out", response);
        sessionStorage.removeItem('access_token');
        this.router.navigate(['/login']);

      },
      error: err => {
        console.log("something went wrong", err)
      }
    })
  }

}
