import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-portfolio',
  imports: [CommonModule, RouterModule],
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent{

  technologies = ["Java", "Spring Boot", "Maven", "HTML", "Javascript", "Angular 14", "Postgresql", "AEM", "Git", "Azure devops", "Linux", "Docker", "CI/CD",
     "Salesforce Marketing cloud"];

  companies: {name : string, timeLine : string, location: string, role : string}[] = [
    {
      name : "Wipro Technologies",
      timeLine : "2018 - 2020",
      location: "Bangalore",
      role: "Project Engineer"
    },
    {
      name : "Infosys Limited",
      timeLine : "2020 - 2022",
      location: "Mysore/Amsterdam",
      role: "Techonolgy Analyst"
    },
    {
      name : "Exact B.V.",
      timeLine : "2022 - Permanent",
      location: "Amsterdam",
      role: "Software Engineer"
    },
  ]

  Achievements : {descriptions : string}[] = [
    
  ]
  
  scrollTech : boolean = true;
  
   @ViewChild('scrollContainer') scrollContainer!: ElementRef<HTMLDivElement>;
   @ViewChildren('achievements') achievements!: QueryList<ElementRef<HTMLElement>>;

  ngAfterViewInit() {
    //this.startInfiniteScroll();
    this.startScroll();
    this.achievements.forEach( (elementRef : ElementRef<HTMLElement>) =>{
      this.observer.observe(elementRef.nativeElement);
    });
  }

observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('show');
        }
        if(!entry.isIntersecting){
          entry.target.classList.remove('show');
        }
      });
}, { threshold: 0.2 });
  

startScroll() {
  const container = this.scrollContainer.nativeElement;
  const children = Array.from(container.children) as HTMLElement[];
  const speed = 1;

  // store initial X positions
  const startPositions = children.map((el, i) => {
    let x = 0;
    for (let j = 0; j < i; j++){
      x += children[j].offsetWidth + 20;
    } 
    return x;
  });

   // store initial X positions
  const endPositions = children.map((el, i) => {
    let x = 0;
    for (let j = 0; j < i; j++){
      x += children[j].offsetWidth + 20;
    } 
    return x+el.offsetWidth;
  });

  console.log("start Positions are "+startPositions+ " end Positions are "+endPositions)

  const step = () => {
    if(this.scrollTech){
    for (let i = 0; i < children.length; i++) {
      startPositions[i] -= speed;
      endPositions[i] -= speed;
      // if startPositions left, move to the end
      if (startPositions[i] + children[i].offsetWidth < 0) {
        const maxX = Math.max(...endPositions);   
        startPositions[i] = maxX + 20;
        endPositions[i] = startPositions[i] + children[i].offsetWidth;
      }
      children[i].style.transform = `translateX(${startPositions[i]}px)`;
    }
    }
    requestAnimationFrame(step);
  };
  requestAnimationFrame(step);
}

}
