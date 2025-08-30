import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShowtimesService {

  constructor(private http : HttpClient) { }

  private registerShowTimeUrl = 'http://localhost:8080/showtime/register'
  private getSlotsUrl = 'http://localhost:8080/screen/showtime/available'
  private getShowsUrl = 'http://localhost:8080/movies/'
  private getShowSeatsUrl = 'http://localhost:8080/showtime/seats/'
  private bookTicketsUrl = 'http://localhost:8080/showtime/book/tickets'
  private getTicketsUrl = 'http://localhost:8080/showtime/download/tickets'

  getShows(id:number):Observable<any>{
    //const url = `${this.getShowsUrl}${id}/showTimes`;
    const url = `http://localhost:8080/showtime/shows/${id}`
    console.log(url);
    return this.http.get(url);
  }

  getAvailableSlots(data:{}):Observable<any>{
    return this.http.post(this.getSlotsUrl, data);
  }

  getAvailableDays(id:number):Observable<any>{
    const getDaysUrl = `http://localhost:8080/screen/${id}/dates`;
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
