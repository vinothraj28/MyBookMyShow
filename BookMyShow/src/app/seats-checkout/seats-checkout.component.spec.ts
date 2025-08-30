import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeatsCheckoutComponent } from './seats-checkout.component';

describe('SeatsCheckoutComponent', () => {
  let component: SeatsCheckoutComponent;
  let fixture: ComponentFixture<SeatsCheckoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeatsCheckoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SeatsCheckoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
