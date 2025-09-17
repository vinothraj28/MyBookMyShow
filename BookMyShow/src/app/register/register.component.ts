import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginService } from '../services/loginService/login.service';
import { ToastServiceService } from '../services/toastService/toast-service.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  today!: string;
  // to check form values and status
  ngOnInit() {
    this.registerForm.statusChanges.subscribe(status => {
      console.log("Form status:", status);
      console.log("Form values:", this.registerForm.value);
    });
    this.today = this.getDate();
    console.log("today", this.today);
  }

   countries : string[] = ['Netherlands', 'India', 'Germany', 'US'];

  registerForm = new FormGroup({
    username : new FormControl('', [Validators.required]),
    email : new FormControl('', [Validators.required, Validators.email, Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')]),
    dob : new FormControl('', [Validators.required]),
    password : new FormControl('', [Validators.required]),
    rePassword : new FormControl('', [Validators.required]),
    country : new FormControl('', [Validators.required])
  });

  constructor(private loginService : LoginService, private router : Router
    , private toastService : ToastServiceService){}


  onRegister():void{

    if(!this.registerForm.valid){
      return;
    }

    const formData = {
      userName : this.registerForm.get('username')?.value,
      email : this.registerForm.get('email')?.value,
      dob : this.registerForm.get('dob')?.value,
      password : this.registerForm.get('password')?.value,
      country : this.registerForm.get('country')?.value,
      //adding no roles for now
      roles :[{
        roleName : "USER"
      }]
    }

    console.log(formData);

    this.loginService.register(formData).subscribe({
      next: response => {
        console.log("User registered", response);
        this.router.navigate(['/login']);
        this.toastService.displayMessage("User Registered Successfully", "success");
      },
      error : error => {
        console.log("Error", error)
        this.toastService.displayMessage("User Registeration failed", "danger");
      }
    });

  }

  getDate(): string {
        const now = new Date();
        const month = (now.getMonth() + 1).toString().padStart(2, '0');
        const day = now.getDate().toString().padStart(2, '0');
        return `${now.getFullYear()}-${month}-${day}`;
      }

}
