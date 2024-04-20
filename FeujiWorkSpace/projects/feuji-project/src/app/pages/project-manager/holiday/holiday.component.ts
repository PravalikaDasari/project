import { Component, OnInit } from '@angular/core';
import { holidayRepo } from '../../../../models/holiday.repo';
import { HolidayService } from '../../../../models/holidayservice.service';
import { Holiday} from '../../../../models/holiday.model';

@Component({
  selector: 'app-holiday',
  templateUrl: './holiday.component.html',
  styleUrl: './holiday.component.css'
})
export class HolidayComponent  implements OnInit
{
  public holidays:Holiday[]=[];
  constructor(private repository:holidayRepo,private data:HolidayService){}
  ngOnInit(): void {
      this.getholiday()
  }
  getholiday(){
this.data.getholiday().subscribe(d=>{
  this.holidays=d;
})
}

}
