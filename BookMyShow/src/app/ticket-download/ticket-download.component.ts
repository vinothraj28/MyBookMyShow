import { Component } from '@angular/core';
import { ShowtimesService } from '../services/showtimes/showtimes.service';

@Component({
  selector: 'app-ticket-download',
  imports: [],
  templateUrl: './ticket-download.component.html',
  styleUrl: './ticket-download.component.css'
})
export class TicketDownloadComponent {

constructor(private showtimeService : ShowtimesService){}


  getTicktes(){
    const formData = JSON.parse(<string>localStorage.getItem('ticket'));
    this.showtimeService.getTickets(formData).subscribe({
      next: (response:Blob) => {
        const url = URL.createObjectURL(response);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'booking.pdf'; // same filename as backend
        a.click();
        window.URL.revokeObjectURL(url);
      }, error: err => {
        console.log(err);
      }
    })
  }

}
