import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  http = inject(HttpClient);
  private baseUrl = environment.apiUrl;

  private loginUrl : string = `${this.baseUrl}/login`;
  private registerUrl : string = `${this.baseUrl}/register`;
  private tokenValidUrl : string = `${this.baseUrl}/isTokenValid`;
  private logoutUrl : string =`${this.baseUrl}/logout`;

  //login method to authenticate user and retrieve the token
  login( username:string, password:string ) :Observable<any>{
    const credentals = {username, password}
    return this.http.post(this.loginUrl, credentals);
  }

  //logout the user
  logout() : Observable<any>{
    return this.http.get(this.logoutUrl);
  }


  //register method to
 register(data:{}) : Observable<any>{
  return this.http.post(this.registerUrl, data);
 }


 //check if a token is valid
 isTokenValid(): Observable<boolean>{
  return this.http.get<boolean>(this.tokenValidUrl).pipe(
    catchError(() => of(false))
  );
 }
  

}
