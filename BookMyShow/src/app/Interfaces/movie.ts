import { ShowTime } from "./ShowTime";

export interface movie{
    id:number,
    name:string,
    language:string,
    movieHrs : number,
    rating:string,
    showTimes : ShowTime[],
    movieImageId : number
}