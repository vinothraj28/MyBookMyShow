import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-base-nav',
  imports: [RouterOutlet, RouterModule],
  templateUrl: './base-nav.component.html',
  styleUrl: './base-nav.component.css'
})
export class BaseNavComponent {

}
