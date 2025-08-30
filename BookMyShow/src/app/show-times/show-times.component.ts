import { Component, NgModule } from '@angular/core';
import { ShowtimesService } from '../services/showtimes/showtimes.service';
import { MovieService } from '../services/movie/movie.service';
import { movie } from '../Interfaces/movie';
import { CommonModule } from '@angular/common';
import { TheaterService } from '../services/theaterServices/theater.service';
import { UserserviceService } from '../services/user/userservice.service';
import { Theater } from '../theater/theater.component';
import { NgSelectComponent } from '@ng-select/ng-select';
import { MatDatepickerInputEvent, MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ShowTime } from '../Interfaces/ShowTime';

@Component({
  selector: 'app-show-times',
  imports: [CommonModule, ReactiveFormsModule, FormsModule, NgSelectComponent, MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatIconModule],
  templateUrl: './show-times.component.html',
  styleUrl: './show-times.component.css'
})
export class ShowTimesComponent {

  constructor(private showtimeService : ShowtimesService, private movieService : MovieService,
    private theaterService : TheaterService, private userService : UserserviceService
  ){}

  showTimeForm = new FormGroup({
    theaterId : new FormControl(0, Validators.required),
    screenId : new FormControl(0, Validators.required),
    date : new FormControl(Date,Validators.required ),
    showSlots : new FormControl('', Validators.required)
  })

  dateError : string ="";
  //get and store the available dates from backend
  availableDates : Date[] = [];
  
  selectedDate:Date| null= null;

  //Get the Lits of movies from backend and save it
  movies : movie[] = [];
  //Get the List of theater from backend and save it
  TheaterList : Theater[] = [];
  //Assining a object with Image ID and it's blob URL
  movieImgUrl : {[id:number]:string} = {};
  //To check if we are adding a show time
  addingShowTime : boolean = false;

  //Get the available show time slots from backend
  showTimeSlots!: ShowTime[];

  //Get the show Start Time and End Time Value
  showStartTime! : Date | null;
  showEndTime! : Date | null;

  //Get the Selected ShowTime Movie details to display it in the FORM
  showTimeMovie : movie | null= { 
    id:0,
    name:'',
    language:'',
    movieHrs : 0,
    rating:'',
    showTimes : [],
    movieImageId : 0
  };

  //Get the Selected theater from form Input and save it's details
  selectedTheater = {
     id: 0,
     theaterName: '',
     screenCount: 0,
     screens:[ {"id": 0,
            "name": "",
            "rows": 0,
            "columns": 0}]
  };

  ngOnInit(){
    this.updateMovieList();
  }

  updateMovieList(){
    this.movieService.getMovies().subscribe({
      next:response => {
        console.log(response);
        this.movies =response as movie[];
        this.movies.forEach( (element : movie)=>{
          let id = element.movieImageId;
          console.log("Movie image id is "+id)
          this.movieService.getMovieImage(id).subscribe({
            next: (response :Blob)=> {
              console.log(response);
              const url = URL.createObjectURL(response);
                this.movieImgUrl[id] = url;
            }, error : err => {
              console.log(err)
            }
          })
          
        })
      },
      error : err => {
        console.log(err)
      }
    })

  }

  addShowTime(movie : movie){
    this.addingShowTime = true;
    //Assign the show time movie to display it in the FORM
    this.showTimeMovie = movie;
    console.log("showTimeMovie", this.showTimeMovie)
    const data = {userId : this.userService.getUserDetails()?.id}
    this.theaterService.getTheaters(data).subscribe({
      next:response => {
        console.log(response);
        //Get the theaterList to display it in the FORM
        this.TheaterList = response;
      }
      ,error:err => { console.log(err)}
    })
  }

  //Once the user selects a theater, retrieve it's screen details
  getScreenOptions(theater:{id:number}){
    console.log(theater.id)
    this.theaterService.getScreens(theater.id).subscribe({
      next:response => {
        console.log(response);
        this.selectedTheater = response;
        //update the label for the screens to use in ng-select
        this.updateScreenOptions();
      }
    })

  }

  //update the label for the screen to use in ng-selct
  updateScreenOptions(){
    this.selectedTheater.screens = this.selectedTheater.screens.map( (screen, i) => ({
      ...screen,
      label:"screen_" + (i+1).toString()
    }));
    console.log("Updated screen is", this.selectedTheater.screens)
  }

  getUpdatedSlots(){
    this.showTimeSlots = this.showTimeSlots.map( (slot) => ({
      ...slot, label : slot.startTime + " "+ slot.endTime
    }) );
  }


  //Once the user select the screen, get the available days for the screen
  getAvaiableDays(event:any){
    console.log("Screen details to get the available days",event, this.showTimeMovie)
    //Below value is being replace with formControlNAme screenId
    //this.showTimeScreen = event;
    this.showtimeService.getAvailableDays(event.id).subscribe({
      next:response => {
        console.log(response)
        this.availableDates = response;
      }, error: err => {
        console.log(err)

      }
    })
  }

  //Filter the dates to be displayed in MAtDatePicker based on the available dates
  dateFilter = (date: Date | null): boolean => {
    if (!date) return false;
    return this.availableDates.some(avail =>
      new Date(avail).toDateString() === date.toDateString()
    );
  };

  //Check for each date change
  isDateValid(event:MatDatepickerInputEvent<Date, Date | null>){
   const dateValue = this.showTimeForm.get('date')?.value;

    if (! (dateValue instanceof Date)) {
      this.dateError = "Date is null or not a Date";
      return;
    } 

    if(!dateValue) {
      this.dateError="Select a date"
      return
    }
      
    if(! (this.availableDates.some( avail => new Date(avail).toDateString() === dateValue?.toDateString()))){
      this.dateError = "Select a Valid date";
      return;
    }
    
    this.dateError = "";

    const formData = new FormData();
    const screenId = this.showTimeForm.get("screenId")?.value;
    formData.append("screenId", this.showTimeForm.get("screenId")?.value?.toString() ?? "");
    formData.append("duration", this.showTimeMovie!.movieHrs.toString());
    formData.append("date", dateValue.toLocaleDateString('en-CA'));

    this.showtimeService.getAvailableSlots(formData).subscribe({
      next: response => {
        console.log(response)
        this.showTimeSlots = response;
        this.getUpdatedSlots();
      }, error : err => {
        console.log(err);
      }
    });
  }

  selectShowTime(event : ShowTime){
    console.log(event);
    this.showStartTime = event.startTime;
    this.showEndTime = event.endTime;    
  }


  registerShowTime(){
    const dateValue = this.showTimeForm.get('date')?.value;
    if (! (dateValue instanceof Date)) {
      this.dateError = "Date is null or not a Date";
      return;
    } 

    const formData = {
      movieId : this.showTimeMovie?.id,
      screenId : this.showTimeForm.get("screenId")?.value,
      userId : this.userService.getUserDetails()?.id,
      showStartTime : this.showStartTime,
      showEndTime : this.showEndTime,
      date : dateValue.toLocaleDateString('en-CA')
    }

   this.showtimeService.addShowTime(formData).subscribe({
      next:response => {
        console.log(response);
      this.showTimeForm.reset();

      }, error : err => {
        console.log(err)
      }
    });
  }

}
