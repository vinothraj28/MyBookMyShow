import { Component, NgModule } from '@angular/core';
import { Theater } from '../theater/theater.component';
import { UserserviceService } from '../services/user/userservice.service';
import { TheaterService } from '../services/theaterServices/theater.service';
import { CommonModule, NumberSymbol } from '@angular/common';
import {  FormsModule, } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { ToastServiceService } from '../services/toastService/toast-service.service';


@Component({
  selector: 'app-theater-list',
  imports: [FormsModule, CommonModule, NgSelectModule],
  templateUrl: './theater-list.component.html',
  styleUrl: './theater-list.component.css'
})
export class TheaterListComponent {

  constructor(private userService:UserserviceService, private theaterService : TheaterService, private toastService : ToastServiceService) { }

  numbers : number[] = Array.from({length:25}, (_, i) => i + 1);
  TheaterList : Theater[] = [];
  userId:number=0;

  
  selectedScreenId = null;
  selectedScreen : any = null;

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
    this.userService.userSource$.subscribe( userId => {
      this.userId = userId.id;
       this.getTheater();
    });
  }

  //updates the screen array to make use in ng-select
  getScreenOptions() {
    this.selectedTheater.screens = this.selectedTheater.screens.map((screen, i) => ({
    ...screen, // copy the existing object
    label:"screen_" + (i + 1).toString()  // index starting at 1 as string
  }));
  console.log("UPdated selected Theater", this.selectedTheater)
}

//Used to get a list of theaters
  getTheater(){
    const data = {userId : this.userService.getUserDetails()?.id}
    this.theaterService.getTheaters(data).subscribe({
      next:response => {
        console.log(response);
        this.TheaterList = response;
    
      }
      ,error:err => { console.log(err)}
    })
    
  }


  //Used to get the theater details and also update the screen array to make use in ng-select
  editTheater(theater:Theater ,event:Event){
    event.stopPropagation();
    // Reset form variables immediately to clear previous values
    this.selectedTheater = {
      id: 0,
      theaterName: '',
      screenCount: 0,
      screens: [ {"id": 0,
            "name": "",
            "rows": 0,
            "columns": 0}]
    };
    this.selectedScreenId = null;
    this.selectedScreen = null;

    console.log(theater);
    this.theaterService.getScreens(theater.id).subscribe({
      next:response => {
        console.log(response);
        this.selectedTheater = response;
        this.getScreenOptions();
      }
    })
  }

  onScreenChange(){
    console.log(typeof this.selectedScreenId);
    if (this.selectedScreenId != null && this.selectedScreenId !== 0) {
    const selectedIdNum = Number(this.selectedScreenId);  // convert to number
    this.selectedScreen = this.selectedTheater.screens.find(
      screen => screen.id === selectedIdNum
    );
    console.log(this.selectedScreen);
  }
 }

 updateSelectedTheater(){
  console.log(this.selectedScreen);
  this.theaterService.updateScreen(this.selectedScreen).subscribe({
    next: response => {
      console.log(response);
      this.toastService.displayMessage("Theater Updated Sucessfully!", "success");
    }
  })
 }
   

}
