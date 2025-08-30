import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseNavComponent } from './base-nav.component';

describe('BaseNavComponent', () => {
  let component: BaseNavComponent;
  let fixture: ComponentFixture<BaseNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BaseNavComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BaseNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
