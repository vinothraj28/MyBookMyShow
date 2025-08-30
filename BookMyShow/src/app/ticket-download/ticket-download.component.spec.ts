import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketDownloadComponent } from './ticket-download.component';

describe('TicketDownloadComponent', () => {
  let component: TicketDownloadComponent;
  let fixture: ComponentFixture<TicketDownloadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketDownloadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketDownloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
