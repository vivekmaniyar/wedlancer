using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Inquiries
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public long? PhoneNumber { get; set; }
        public string Email { get; set; }
        public string Message { get; set; }
        public string Status { get; set; }
    }
}
