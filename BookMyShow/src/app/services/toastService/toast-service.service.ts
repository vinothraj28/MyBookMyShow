import { Injectable } from '@angular/core';
import { BehaviorSubject, timer } from 'rxjs';

export interface Toast{
  message:string,
  type : "success" | "danger" | "info";
}

@Injectable({
  providedIn: 'root'
})
export class ToastServiceService {

  private toast = new BehaviorSubject<Toast | any>(null);
  toast$ = this.toast.asObservable();

  displayMessage(message:string, type:"success" | "danger" | "info", duration : number = 3000){
    this.toast.next({message, type});

    timer(duration).subscribe(() => {
       this.toast.next(null);
    })

  }

}
