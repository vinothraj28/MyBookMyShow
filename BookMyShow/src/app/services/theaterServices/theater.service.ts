import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserserviceService } from '../user/userservice.service';

@Injectable({
  providedIn: 'root'
})
export class TheaterService {

  http = inject(HttpClient);
  user = inject(UserserviceService);

  
  private theaterListUrl = 'http://localhost:8080/theater/list';
  private theaterAddUrl = 'http://localhost:8080/theater/register';
  private screenListUrl = 'http://localhost:8080/theater/{id}/screens';
  private updateScreenUrl = 'http://localhost:8080/theater/screen/update';

  addTheater(data:{}):Observable<any>{
    return this.http.post(this.theaterAddUrl, data);
  }

  getTheaters(data : {}):Observable<any>{
    return this.http.post(this.theaterListUrl, data)
  }

  //get list of screens and their data
  getScreens(id: number):Observable<any>{
    const url = `http://localhost:8080/theater/${id}/screens`;
    return this.http.get(url);
  }

  //update the rows & columns in the screen
  updateScreen(data:any) : Observable<any>{
    const userId = this.user.getUserDetails().id;
    data.userId = this.user.getUserDetails().id
    data.screenId = data.id;
    console.log(data);
    return this.http.post(this.updateScreenUrl, data);
  }

}
