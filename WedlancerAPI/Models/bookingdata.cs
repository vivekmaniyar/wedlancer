using System;

namespace WedlancerAPI.Models
{
    public class bookingdata
    {
        public int BookingId { get; set; }
        public string freelancerusername { get; set; }
        public string employerusername { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public string Status { get; set; }
    }
}
