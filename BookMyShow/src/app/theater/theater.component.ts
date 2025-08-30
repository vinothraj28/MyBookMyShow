import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { setCurrentInjector } from '@angular/core/primitives/di';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { UserserviceService } from '../services/user/userservice.service';
import { TheaterService } from '../services/theaterServices/theater.service';
import { ToastServiceService } from '../services/toastService/toast-service.service';
import { last, filter } from 'rxjs';
import { disableDebugTools } from '@angular/platform-browser';

export interface Theater{
  id: number,
  name: string,
  screenCount: number
}

@Component({
  selector: 'app-theater',
  imports: [ReactiveFormsModule, CommonModule, NgSelectModule ],
  templateUrl: './theater.component.html',
  styleUrl: './theater.component.css'
})
export class TheaterComponent {

  numbers: { label: string; screenName: string; value: number | null; disabled: boolean }[] =  [
    {label: "Select the screen count", screenName:'' ,value:null, disabled:true },
    ...Array.from({length:30}, (_, i) => ({
      label:(i+1).toString(),
      screenName:"Screen_" + (i+1).toString() ,
       value: (i+1), 
       disabled:false
    }))
  ]
 
  userId : number = 0;
  currentScreenIndex : number = 0;
  successMessage:boolean =false;

  ngOnInit(){ 
    this.userService.userSource$.pipe(filter(user => user != null)).subscribe( user => {
      this.userId = user.id;
    })
    this.TheaterForm.valueChanges.subscribe(()=>{
      console.log(this.TheaterForm.value)
    })
    this.TheaterForm.get('screenCount')?.valueChanges.subscribe((count) => {
    this.setScreens(Number(count));
    }); 
  }

  constructor(private userService:UserserviceService, private theaterService : TheaterService, 
    private toastService : ToastServiceService
  ) { }

  TheaterForm = new FormGroup({
    Name : new FormControl('',[Validators.required]),
    screenCount : new FormControl('', [Validators.required]),
    screens : new FormArray<FormGroup>([])
  })

  get screens():FormArray{
    return this.TheaterForm.get('screens') as FormArray;
  }

  getScreenGroup(index: number): FormGroup {
    return this.screens.at(index) as FormGroup;
  }

  setScreens(count:number){
    const screensArray = new FormArray<FormGroup>([]);
    for(let i=0; i<count; i++){
      screensArray.push( 
        new FormGroup({
          screenName: new FormControl(`Screen ${i + 1}`, Validators.required),
          rows: new FormControl(null, [Validators.required, Validators.min(1), Validators.pattern(/^\d+$/)]),
          columns: new FormControl(null, [Validators.required, Validators.min(1), Validators.pattern(/^\d+$/)])
        }));
    }
    this.TheaterForm.setControl('screens',screensArray)
  }

  nextScreen(){
    if(this.screens.at(this.currentScreenIndex).valid){
      console.log("Clicking on next")
      
      this.currentScreenIndex++;
    }else{
      this.toastService.displayMessage("Please fill in the details", "danger");
    }
  }

  previousScreen(){
    if(this.currentScreenIndex!=0){
      this.currentScreenIndex--;
    }  
  }
 

  addTheater(){
    console.log(this.TheaterForm.get('Name')?.value);
    console.log(this.TheaterForm.get('screenCount')?.value)
    console.log(this.userService.getUserDetails()?.id);

    const TheaterFormValue = this.TheaterForm.value;

    const formData = {
      name : TheaterFormValue.Name,
      screenCount : TheaterFormValue.screenCount,
      screens : TheaterFormValue.screens,
      userId : this.userId
    };

    this.theaterService.addTheater(formData).subscribe({
      next:response => {
        console.log("Theater added", response);
        this.toastService.displayMessage("Theater Successfully Added", "success");
        this.TheaterForm.reset();
        this.screens.clear;
      }, error: err=>{
        console.log(err);
      }
    })
  }


}
