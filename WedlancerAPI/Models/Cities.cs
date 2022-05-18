using Newtonsoft.Json;
using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Cities
    {
        public Cities()
        {
            Profiles = new HashSet<Profiles>();
        }

        public int CityId { get; set; }
        public string CityName { get; set; }
        public int StateId { get; set; }

        public virtual States State { get; set; }
        [JsonIgnore]
        public virtual ICollection<Profiles> Profiles { get; set; }
    }
}
