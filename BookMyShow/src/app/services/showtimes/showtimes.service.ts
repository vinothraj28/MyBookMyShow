import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ShowtimesService {

  constructor(private http : HttpClient) { }
  private baseUrl = environment.apiUrl;
  private registerShowTimeUrl = `${this.baseUrl}/showtime/register`;
  private getSlotsUrl = `${this.baseUrl}/screen/showtime/available`;
  private getShowsUrl = `${this.baseUrl}/movies/`;
  private getShowSeatsUrl = `${this.baseUrl}/showtime/seats/`;
  private bookTicketsUrl = `${this.baseUrl}/showtime/book/tickets`;
  private getTicketsUrl = `${this.baseUrl}/showtime/download/tickets`;

  getShows(id:number):Observable<any>{
    //const url = `${this.getShowsUrl}${id}/showTimes`;
    const url = `${this.baseUrl}/showtime/shows/${id}`
    console.log(url);
    return this.http.get(url);
  }

  getAvailableSlots(data:{}):Observable<any>{
    return this.http.post(this.getSlotsUrl, data);
  }

  getAvailableDays(id:number):Observable<any>{
    const getDaysUrl = `${this.baseUrl}/screen/${id}/dates`;
    return this.http.get(getDaysUrl);
  }

  addShowTime(data : {}):Observable<any>{
    return this.http.post(this.registerShowTimeUrl, data);
  }

  getSeats(id:number):Observable<any>{
    const url = `${this.getShowSeatsUrl}${id}`
    return this.http.get(url);
  }

  bookTickets(data:{}):Observable<any>{
    return this.http.post(this.bookTicketsUrl, data);
  }

  getTickets(data:{}):Observable<any>{
    return this.http.post(this.getTicketsUrl, data, { responseType: 'blob' });
  }
}
