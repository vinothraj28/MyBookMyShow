import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { movie } from '../../Interfaces/movie';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  constructor() { }

  private bookingSource = new BehaviorSubject<any>(null);
  $booking = this.bookingSource.asObservable();

  setBookingDetails(data:movie){
    sessionStorage.setItem('booking', JSON.stringify(data));
    this.bookingSource.next(data);
  };

  getBookingDetails(){
    return this.bookingSource.value ?? JSON.parse(<string>sessionStorage.getItem('booking'));
  }

}
