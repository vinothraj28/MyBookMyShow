import { Component, ElementRef, ViewChild } from '@angular/core';
import { movie } from '../Interfaces/movie';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MovieService } from '../services/movie/movie.service';
import { BookingService } from '../services/bookingService/booking.service';
import { CommonModule } from '@angular/common';
import { ShowtimesService } from '../services/showtimes/showtimes.service';
import { FormsModule } from "@angular/forms";
import { seats } from '../Interfaces/seats';
import { user, UserserviceService } from '../services/user/userservice.service';



export interface ShowTime {
  id: number;
  date: Date;
  showStartTime: string;
  showEndTime: string;
}

@Component({
  selector: 'app-booking-page',
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './booking-page.component.html',
  styleUrl: './booking-page.component.css'
})
export class BookingPageComponent {

  movie : movie = { 
    id:0,
    name:'',
    language:'',
    movieHrs : 0,
    rating:'',
    showTimes :[],
    movieImageId : 0
  };
  user!: user;
  selectedShow : number = 0;
  movieImgUrl : {[id:number]:string} = {};
  seatRows: { [key: string]: seats[] } = {};
  seats: seats[] = [];
  selectedSeats: seats[] = [];

  selectedDate: {[key:string]:boolean} = {};
  selectedShowNumber: {[key:string]:boolean} = {};

  showDates : { [key:string]:{ showStartTime: Date; showEndTime: Date, id : number, 
              screenName: string, theaterName : string,selected : boolean}[]} = {};
  showTimes :{[key:string] :{ showStartTime: Date; showEndTime: Date, id : number,screenName: string, theaterName : string  ,  selected : boolean}[]} = {};

  //if Scrollable
  canScrollLeft : boolean = false;
  canScrollRight : boolean = false;
  canScrollBottom : boolean = false;

  @ViewChild('dateCarousel', {static: false}) dateCarousel! : ElementRef;
  @ViewChild('bottomAnchor', {static:false}) bottomAnchor! : ElementRef;
  
  

  constructor(private router:Router, private bookingService : BookingService , 
    private showTimeService : ShowtimesService ,private movieService : MovieService, private userService : UserserviceService){}

  ngAfterViewInit(){
    this.updateScrollButtons();
  }

  ngAfterViewChecked(){
    this.scrollToBottom();
  }


  scrollToBottom(){
    if(this.canScrollBottom){
      this.bottomAnchor.nativeElement.scrollIntoView({behavior:'smooth'});
      this.canScrollBottom = false;
    }
  }

  ngOnInit(){
    this.bookingService.$booking.subscribe({
      next: response =>{
        this.movie = this.bookingService.getBookingDetails();     
        console.log(this.movie)
      }, error:err =>{
        console.log(err);
      }
    });
    this.getMovieImage();
    this.getShowTimes();
    this.userService.userSource$.subscribe({
      next: response => { this.user = response; }
    })
    console.log("user", this.user)
  
  }

  compareDates(a:any, b:any):number{
    return new Date(a.key).getTime() - new Date(b.key).getTime();
  }

  updateScrollButtons(){
    const el = this.dateCarousel.nativeElement;

    this.canScrollLeft = el.scrollLeft > 0;
    this.canScrollRight = el.scrollLeft + el.clientWidth < el.scrollWidth;

    console.log(this.canScrollLeft, this.canScrollRight);
  }

  getSelectedSeats(){
    return this.selectedSeats.map( s => s.seatNumber).join(',');
  }

  generateDateOption(){
    this.movie.showTimes.forEach((show)=> {
      const date = new Date(show.date);
      console.log("Each show is ", show)
      const key = this.formatDate(date);
      if(!this.showDates[key]){
        this.showDates[key] = []
      }
      this.showDates[key].push({showStartTime: show.startTime, showEndTime : show.endTime, id: show.id, screenName : show.screenName,
        theaterName : show.theaterName, selected : false})
    });
    console.log("Generated dates are",this.showDates);
  }


  formatDate(d: Date): string {
    const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    return `${months[d.getMonth()]} ${weekdays[d.getDay()]} ${d.getDate()}`;
  }

  scrollDateCaraousel(direction : 'left' | 'right'){
    //const container = document.querySelector('.date-caraousel');
    const container = this.dateCarousel.nativeElement;
    if(container){
      const scroll = 200;
      container.scrollBy({
        left: direction==='right' ? scroll : -scroll,
        behavior: 'smooth'
      })
    }
    setTimeout(()=> this.updateScrollButtons(), 300);
  }

  selectDate(option: { showStartTime: Date; showEndTime: Date, id : number,screenName: string, theaterName : string  ,  selected : boolean}[]) {
    console.log("Shows for selected date" ,option);
    this.seatRows = {};
    this.selectedSeats = [];
    this.showTimes = {};
    option.forEach( (shows) => {
      if(!this.showTimes[shows.theaterName]){
        this.showTimes[shows.theaterName] = [];
      }
      this.showTimes[shows.theaterName].push(shows);
    })

  }


  getMovieImage(){
     this.movieService.getMovieImage(this.movie.movieImageId).subscribe({
            next: (response:Blob) =>{
              let url = URL.createObjectURL(response);
              this.movieImgUrl[this.movie.movieImageId] = url;
            }, error:err =>{
              console.log(err)
            }
          })
  }


  buildSeatRows() {
  this.seatRows = {};
  this.seats.forEach(seat => {
    const row = seat.seatNumber.charAt(0); // first letter as row
    if (!this.seatRows[row]) {
      this.seatRows[row] = [];
    }
    this.seatRows[row].push(seat);
  });
  this.canScrollBottom = true;
}



isSelectedSeat(seat: seats) {
  return this.selectedSeats.some(s => s.seatNumber === seat.seatNumber);
}

toggleSeatSelection(seat: seats) {
  
  const idx = this.selectedSeats.findIndex(s => s.seatNumber === seat.seatNumber);
  if (idx > -1) {
    this.selectedSeats[idx].userName = '';
    this.selectedSeats.splice(idx, 1);
  }else{
    seat.userName = this.user.userName;
    this.selectedSeats.push(seat); 
    this.canScrollBottom = true
  }
  
 
}

  getShowTimes(){
    this.showTimeService.getShows(this.movie.id).subscribe({
      next: response => {
        console.log(response)
        this.movie.showTimes = response;
        this.generateDateOption();
      }
    })

  }

  bookSeat(event : any){
    console.log(event)
    this.seatRows = {};
    this.selectedShowNumber = {}; 
    this.selectedShowNumber[event.id] = true; 
    this.selectedShow = event.id;
    this.canScrollBottom = true ;
    this.selectedSeats = [];
    this.showTimeService.getSeats(this.selectedShow).subscribe({
      next: response =>{
        console.log(response);
        this.seats = response;
        this.buildSeatRows();
      }, error : err => {
        console.log(err);
      }
    })
  }

  bookTickets(){
    this.selectedSeats = this.selectedSeats.map( s => ({
      ...s,
      userName : this.user.userName}));
    const formdata = {
      showTimeID: this.selectedShow,
      seats : this.selectedSeats,

    }
    this.showTimeService.bookTickets(formdata).subscribe({
      next: response => {
        console.log(response);
        localStorage.setItem("ticket", JSON.stringify(formdata));
        this.router.navigate(['/base/ticket-download']);
      }
    });

  }

}
