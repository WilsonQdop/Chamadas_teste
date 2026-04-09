import { TestBed } from '@angular/core/testing';

import { CalledService } from './called.service';

describe('CalledService', () => {
  let service: CalledService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CalledService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
