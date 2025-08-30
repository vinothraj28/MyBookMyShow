import { Component, OnInit } from '@angular/core';
import { Toast, ToastServiceService } from '../services/toastService/toast-service.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-toast-component',
  imports: [CommonModule],
  templateUrl: './toast-component.component.html',
  styleUrl: './toast-component.component.css'
})
export class ToastComponentComponent implements OnInit {

  toast : Toast | null = null;

  constructor(private toastService : ToastServiceService){}


  ngOnInit(){
     this.toastService.toast$.subscribe( toast => {
      this.toast = toast;
    })
  }

}
