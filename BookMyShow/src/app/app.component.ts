import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { UserserviceService } from './services/user/userservice.service';
import { ToastComponentComponent } from './toast-component/toast-component.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, ToastComponentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'BookMyShow';

  constructor(private userservice: UserserviceService){}

  ngOnInit() {
      console.log('AppComponent initialized');
    const token = sessionStorage.getItem('access_token');
  if (token) {
    console.log("Inside App Component")
    this.userservice.LoadUserData().subscribe({
      next:response => {
        console.log("User data is set", response)
      },
      error: err=> {
        console.log("Unable to get user data");
      }
    })
  }
  }

}
