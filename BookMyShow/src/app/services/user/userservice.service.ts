import { HttpBackend, HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface user {
  id : number,
  userName:string,
  email:string,
  dob:Date,
  country:string,
  roles:[]
}

@Injectable({
  providedIn: 'root'
})
export class UserserviceService {

  http = inject(HttpClient);

  router = inject(Router);
  private baseUrl = environment.apiUrl;
  private userDataUrl = `${this.baseUrl}/user`;

  private userSource = new BehaviorSubject<any>(null);
  userSource$ = this.userSource.asObservable();

  getUserDetails(): user{
    return this.userSource.value;
  }

  setUserDetails(data:user){
    this.userSource.next(data);
    console.log("user set :",this.userSource.value)
  }

  LoadUserData():Observable<any>{
    console.log("loading from backend")
    return this.http.get(this.userDataUrl).pipe(
      tap(user => this.setUserDetails(<user>user))
    );
  }

}
