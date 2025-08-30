import { Component } from '@angular/core';
import { movie } from '../Interfaces/movie';
import { MovieService } from '../services/movie/movie.service';
import { CommonModule } from '@angular/common';
import {  Router, RouterModule } from '@angular/router';
import { CdkAutofill } from "@angular/cdk/text-field";
import { BookingService } from '../services/bookingService/booking.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  movies : movie[] = [];
  movieImgUrl : { [id:number]:string} = {};

  constructor(private movieService : MovieService, private router : Router
    , private bookingService : BookingService
  ){}

  ngOnInit(){
   this.updateMoviesList();
  }

  updateMoviesList(){
     this.movieService.getMovies().subscribe({
      next: response => {
        this.movies = response as movie[];
        this.movies.forEach((movie)=>{
          let imgId = movie.movieImageId;
          this.movieService.getMovieImage(imgId).subscribe({
            next: (response:Blob) =>{
              let url = URL.createObjectURL(response);
              this.movieImgUrl[imgId] = url;
            }, error:err =>{
              console.log(err)
            }
          })
        });
      }, error : err => {
        console.log(err);
      }
    })
  }

  bookMovie(event : movie){
    console.log(event)
    this.bookingService.setBookingDetails(event);
    this.router.navigate(['/base/book']);
  }

}
