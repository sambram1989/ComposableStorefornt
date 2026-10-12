import { ComponentFixture, TestBed } from '@angular/core/testing';

import { W9referralcodeformComponent } from './w9referralcodeform.component';

describe('W9referralcodeformComponent', () => {
  let component: W9referralcodeformComponent;
  let fixture: ComponentFixture<W9referralcodeformComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [W9referralcodeformComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(W9referralcodeformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
