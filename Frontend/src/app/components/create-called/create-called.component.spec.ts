import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCalledComponent } from './create-called.component';

describe('CreateCalledComponent', () => {
  let component: CreateCalledComponent;
  let fixture: ComponentFixture<CreateCalledComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateCalledComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateCalledComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
