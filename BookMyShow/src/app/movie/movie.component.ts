import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgSelectComponent } from '@ng-select/ng-select';
import { MovieService } from '../services/movie/movie.service';
import { ToastServiceService } from '../services/toastService/toast-service.service';

@Component({
  selector: 'app-movie',
  imports: [ReactiveFormsModule, CommonModule, NgSelectComponent],
  templateUrl: './movie.component.html',
  styleUrl: './movie.component.css'
})
export class MovieComponent {

  ngOnInit(){
     this.movieForm.valueChanges.subscribe(()=>{
      console.log(this.movieForm.value)
    })
  }

  constructor(private movieService : MovieService, private toastService : ToastServiceService){}

  Languages : string[] = ["EN", "NL", "DE", "ES"];
  Ratings: string[] = ["A", "U", "UA"];


  movieForm = new FormGroup({
    name : new FormControl('', [Validators.required]),
    language : new FormControl('', [Validators.required]),
    movieHrs : new FormControl('', [Validators.required]),
    rating : new FormControl('', [Validators.required]),
    image : new FormControl<File|null>(null, [Validators.required])
  })

 

  onFileSelected(event : Event){
    const input = event.target as HTMLInputElement;
     if (input.files && input.files.length > 0) {
    const file = input.files[0];
    this.movieForm.get('image')?.setValue(file);
    // Optional: clear validation errors or trigger valueChanges manually
    this.movieForm.get('image')?.markAsTouched();
  }
  }

  registerMovie(){
    console.log("Form Values")
    const formValue = this.movieForm.value;

    if(formValue?.image && formValue.name && formValue.language && formValue.movieHrs && formValue.rating){
      

      const movieDTO = {
        name : formValue?.name ,
        movieHrs :  formValue.movieHrs,
        rating :  formValue.rating,
        language :  formValue.language
      }

      const formData = new FormData();
      formData.append("registerMovieDTO", new Blob([JSON.stringify(movieDTO)], {type: "application/json"}));
      formData.append("multipartFile", formValue?.image);

      
      this.movieService.registerMovie(formData).subscribe({
        next:response => {
          console.log(response);
          this.toastService.displayMessage("Movie added Successfully", "success");
          this.movieForm.reset();
        }
      })
    }
    

    console.log(formValue);

  }




}
